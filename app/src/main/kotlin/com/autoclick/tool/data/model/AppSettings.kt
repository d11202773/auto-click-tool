package com.autoclick.tool.data.model

data class AppSettings(
    val isAccessibilityEnabled: Boolean = false,
    val isOverlayEnabled: Boolean = false,
    val defaultDelayMs: Long = 1000,
    val showOverlay: Boolean = true,
    val vibrateOnClick: Boolean = true,
    val autoStart: Boolean = false,
    val overlayOpacity: Float = 0.8f,
    val clickSoundEnabled: Boolean = false
)
