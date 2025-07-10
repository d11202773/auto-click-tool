package com.autoclick.tool.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import com.autoclick.tool.R
import com.autoclick.tool.data.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class OverlayService : Service() {
    
    companion object {
        private const val TAG = "OverlayService"
    }
    
    @Inject
    lateinit var settingsRepository: SettingsRepository
    
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Overlay service created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (overlayView == null) {
            createOverlay()
        }
        return START_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        removeOverlay()
        serviceScope.cancel()
        Log.d(TAG, "Overlay service destroyed")
    }
    
    private fun createOverlay() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_control, null)
        
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 100
        }
        
        setupOverlayControls()
        setupDragBehavior(layoutParams)
        
        windowManager?.addView(overlayView, layoutParams)
        Log.d(TAG, "Overlay created and added to window")
    }
    
    private fun setupOverlayControls() {
        overlayView?.let { view ->
            val playPauseButton = view.findViewById<ImageButton>(R.id.btn_play_pause)
            val stopButton = view.findViewById<ImageButton>(R.id.btn_stop)
            val minimizeButton = view.findViewById<ImageButton>(R.id.btn_minimize)
            val statusText = view.findViewById<TextView>(R.id.tv_status)
            
            playPauseButton?.setOnClickListener {
                toggleAutoClick()
            }
            
            stopButton?.setOnClickListener {
                stopAutoClick()
            }
            
            minimizeButton?.setOnClickListener {
                minimizeOverlay()
            }
            
            updateOverlayStatus()
        }
    }
    
    private fun setupDragBehavior(layoutParams: WindowManager.LayoutParams) {
        overlayView?.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = layoutParams.x
                    initialY = layoutParams.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    layoutParams.x = initialX + (event.rawX - initialTouchX).toInt()
                    layoutParams.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager?.updateViewLayout(overlayView, layoutParams)
                    true
                }
                else -> false
            }
        }
    }
    
    private fun toggleAutoClick() {
        val service = AutoClickAccessibilityService.getInstance()
        if (service != null) {
            if (service.isRunning()) {
                service.pauseAutoClick()
            } else {
                // Load click points and start
                serviceScope.launch {
                    // Get enabled click points and start service
                    service.resumeAutoClick()
                }
            }
        }
        updateOverlayStatus()
    }
    
    private fun stopAutoClick() {
        AutoClickAccessibilityService.getInstance()?.stopAutoClick()
        updateOverlayStatus()
    }
    
    private fun minimizeOverlay() {
        // Hide overlay temporarily or make it smaller
        overlayView?.visibility = View.GONE
        
        // Show again after 3 seconds
        serviceScope.launch {
            delay(3000)
            overlayView?.visibility = View.VISIBLE
        }
    }
    
    private fun updateOverlayStatus() {
        overlayView?.let { view ->
            val statusText = view.findViewById<TextView>(R.id.tv_status)
            val playPauseButton = view.findViewById<ImageButton>(R.id.btn_play_pause)
            
            val service = AutoClickAccessibilityService.getInstance()
            val isRunning = service?.isRunning() ?: false
            
            if (isRunning) {
                statusText?.text = getString(R.string.status_running)
                playPauseButton?.setImageResource(android.R.drawable.ic_media_pause)
            } else {
                statusText?.text = getString(R.string.status_stopped)
                playPauseButton?.setImageResource(android.R.drawable.ic_media_play)
            }
        }
    }
    
    private fun removeOverlay() {
        overlayView?.let { view ->
            windowManager?.removeView(view)
            overlayView = null
        }
    }
}
