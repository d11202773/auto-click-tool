package com.autoclick.tool.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import com.autoclick.tool.AutoClickApplication
import com.autoclick.tool.R
import com.autoclick.tool.data.model.AutoClickSession
import com.autoclick.tool.data.model.ClickPoint
import com.autoclick.tool.data.repository.ClickPointRepository
import com.autoclick.tool.data.repository.SettingsRepository
import com.autoclick.tool.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class AutoClickAccessibilityService : AccessibilityService() {
    
    companion object {
        private const val TAG = "AutoClickService"
        private const val NOTIFICATION_ID = 1001
        private var instance: AutoClickAccessibilityService? = null
        
        fun getInstance(): AutoClickAccessibilityService? = instance
        
        fun isServiceRunning(): Boolean = instance != null
    }
    
    @Inject
    lateinit var clickPointRepository: ClickPointRepository
    
    @Inject
    lateinit var settingsRepository: SettingsRepository
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val handler = Handler(Looper.getMainLooper())
    
    private var currentSession: AutoClickSession = AutoClickSession()
    private var isExecuting = false
    private var vibrator: Vibrator? = null
    private var overlayService: Intent? = null
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        Log.d(TAG, "Service created")
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Service connected")
        showNotification()
        
        // Update settings that accessibility is enabled
        serviceScope.launch {
            settingsRepository.updateAccessibilityEnabled(true)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
        stopAutoClick()
        serviceScope.cancel()
        stopOverlayService()
        Log.d(TAG, "Service destroyed")
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Handle accessibility events if needed
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Service interrupted")
        stopAutoClick()
    }
    
    fun startAutoClick(clickPoints: List<ClickPoint>) {
        if (isExecuting) {
            Log.w(TAG, "Auto click already running")
            return
        }
        
        if (clickPoints.isEmpty()) {
            Log.w(TAG, "No click points to execute")
            return
        }
        
        currentSession = AutoClickSession(
            id = System.currentTimeMillis().toString(),
            clickPoints = clickPoints.filter { it.isEnabled },
            isRunning = true,
            startTime = System.currentTimeMillis()
        )
        
        isExecuting = true
        startOverlayService()
        executeClickSequence()
        
        Log.d(TAG, "Started auto click with ${currentSession.clickPoints.size} points")
    }
    
    fun stopAutoClick() {
        isExecuting = false
        currentSession = currentSession.copy(isRunning = false)
        handler.removeCallbacksAndMessages(null)
        stopOverlayService()
        Log.d(TAG, "Stopped auto click")
    }
    
    fun pauseAutoClick() {
        if (isExecuting) {
            isExecuting = false
            currentSession = currentSession.copy(
                isRunning = false,
                pauseTime = System.currentTimeMillis()
            )
            handler.removeCallbacksAndMessages(null)
            Log.d(TAG, "Paused auto click")
        }
    }
    
    fun resumeAutoClick() {
        if (!isExecuting && currentSession.clickPoints.isNotEmpty()) {
            isExecuting = true
            currentSession = currentSession.copy(isRunning = true, pauseTime = 0)
            executeClickSequence()
            Log.d(TAG, "Resumed auto click")
        }
    }
    
    private fun executeClickSequence() {
        if (!isExecuting || currentSession.clickPoints.isEmpty()) {
            return
        }
        
        val currentPoint = currentSession.clickPoints[currentSession.currentPointIndex]
        val delay = if (currentSession.currentRepeatCount == 0) 0 else currentPoint.delayMs
        
        handler.postDelayed({
            if (isExecuting) {
                performClick(currentPoint)
                moveToNextPoint()
            }
        }, delay)
    }
    
    private fun performClick(clickPoint: ClickPoint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val path = Path().apply {
                moveTo(clickPoint.x, clickPoint.y)
            }
            
            val gestureBuilder = GestureDescription.Builder()
            val strokeDescription = GestureDescription.StrokeDescription(path, 0, 50)
            gestureBuilder.addStroke(strokeDescription)
            
            val gesture = gestureBuilder.build()
            
            dispatchGesture(gesture, object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    super.onCompleted(gestureDescription)
                    onClickCompleted(clickPoint)
                }
                
                override fun onCancelled(gestureDescription: GestureDescription?) {
                    super.onCancelled(gestureDescription)
                    Log.w(TAG, "Click gesture cancelled")
                }
            }, null)
        }
    }
    
    private fun onClickCompleted(clickPoint: ClickPoint) {
        // Vibrate if enabled
        serviceScope.launch {
            val settings = settingsRepository.settings.first()
            if (settings.vibrateOnClick) {
                vibrate()
            }
        }
        
        // Update session stats
        currentSession = currentSession.copy(
            totalClicks = currentSession.totalClicks + 1
        )
        
        Log.d(TAG, "Clicked at (${clickPoint.x}, ${clickPoint.y}) - ${clickPoint.name}")
    }
    
    private fun moveToNextPoint() {
        val currentPoint = currentSession.clickPoints[currentSession.currentPointIndex]
        val newRepeatCount = currentSession.currentRepeatCount + 1
        
        if (newRepeatCount < currentPoint.repeatCount) {
            // Continue repeating current point
            currentSession = currentSession.copy(currentRepeatCount = newRepeatCount)
        } else {
            // Move to next point
            val nextIndex = (currentSession.currentPointIndex + 1) % currentSession.clickPoints.size
            currentSession = currentSession.copy(
                currentPointIndex = nextIndex,
                currentRepeatCount = 0
            )
        }
        
        executeClickSequence()
    }
    
    private fun vibrate() {
        vibrator?.let { vib ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(50)
            }
        }
    }
    
    private fun startOverlayService() {
        serviceScope.launch {
            val settings = settingsRepository.settings.first()
            if (settings.showOverlay) {
                overlayService = Intent(this@AutoClickAccessibilityService, OverlayService::class.java)
                startForegroundService(overlayService)
            }
        }
    }
    
    private fun stopOverlayService() {
        overlayService?.let {
            stopService(it)
            overlayService = null
        }
    }
    
    private fun showNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, AutoClickApplication.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()
        
        startForeground(NOTIFICATION_ID, notification)
    }
    
    fun getCurrentSession(): AutoClickSession = currentSession
    
    fun isRunning(): Boolean = isExecuting
}
