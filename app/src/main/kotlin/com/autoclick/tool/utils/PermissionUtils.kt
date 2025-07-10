package com.autoclick.tool.utils

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity

object PermissionUtils {
    
    /**
     * Kiểm tra quyền hiển thị overlay
     */
    fun hasOverlayPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }
    
    /**
     * Mở cài đặt quyền overlay
     */
    fun requestOverlayPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
    
    /**
     * Kiểm tra dịch vụ accessibility có được bật không
     */
    fun isAccessibilityServiceEnabled(context: Context, serviceClass: Class<out AccessibilityService>): Boolean {
        val accessibilityServiceName = "${context.packageName}/${serviceClass.name}"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        return enabledServices?.contains(accessibilityServiceName) == true
    }
    
    /**
     * Mở cài đặt accessibility
     */
    fun requestAccessibilityPermission(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
    
    /**
     * Kiểm tra quyền thông báo (Android 13+)
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Implement notification permission check for Android 13+
            true // Placeholder
        } else {
            true
        }
    }
    
    /**
     * Kiểm tra tất cả quyền cần thiết
     */
    fun hasAllRequiredPermissions(context: Context, serviceClass: Class<out AccessibilityService>): Boolean {
        return hasOverlayPermission(context) && 
               isAccessibilityServiceEnabled(context, serviceClass) &&
               hasNotificationPermission(context)
    }
}
