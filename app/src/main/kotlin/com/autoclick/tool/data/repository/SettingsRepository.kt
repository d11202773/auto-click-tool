package com.autoclick.tool.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.autoclick.tool.data.model.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    
    private val _settings = MutableStateFlow(loadSettings())
    val settings: Flow<AppSettings> = _settings.asStateFlow()
    
    private fun loadSettings(): AppSettings {
        return AppSettings(
            isAccessibilityEnabled = sharedPreferences.getBoolean("accessibility_enabled", false),
            isOverlayEnabled = sharedPreferences.getBoolean("overlay_enabled", false),
            defaultDelayMs = sharedPreferences.getLong("default_delay_ms", 1000),
            showOverlay = sharedPreferences.getBoolean("show_overlay", true),
            vibrateOnClick = sharedPreferences.getBoolean("vibrate_on_click", true),
            autoStart = sharedPreferences.getBoolean("auto_start", false),
            overlayOpacity = sharedPreferences.getFloat("overlay_opacity", 0.8f),
            clickSoundEnabled = sharedPreferences.getBoolean("click_sound_enabled", false)
        )
    }
    
    fun updateSettings(settings: AppSettings) {
        sharedPreferences.edit().apply {
            putBoolean("accessibility_enabled", settings.isAccessibilityEnabled)
            putBoolean("overlay_enabled", settings.isOverlayEnabled)
            putLong("default_delay_ms", settings.defaultDelayMs)
            putBoolean("show_overlay", settings.showOverlay)
            putBoolean("vibrate_on_click", settings.vibrateOnClick)
            putBoolean("auto_start", settings.autoStart)
            putFloat("overlay_opacity", settings.overlayOpacity)
            putBoolean("click_sound_enabled", settings.clickSoundEnabled)
            apply()
        }
        _settings.value = settings
    }
    
    fun updateAccessibilityEnabled(enabled: Boolean) {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(isAccessibilityEnabled = enabled))
    }
    
    fun updateOverlayEnabled(enabled: Boolean) {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(isOverlayEnabled = enabled))
    }
    
    fun updateDefaultDelay(delayMs: Long) {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(defaultDelayMs = delayMs))
    }
    
    fun updateShowOverlay(show: Boolean) {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(showOverlay = show))
    }
    
    fun updateVibrateOnClick(vibrate: Boolean) {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(vibrateOnClick = vibrate))
    }
}
