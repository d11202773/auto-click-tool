package com.autoclick.tool.data.model

data class AutoClickSession(
    val id: String = "",
    val clickPoints: List<ClickPoint> = emptyList(),
    val isRunning: Boolean = false,
    val currentPointIndex: Int = 0,
    val currentRepeatCount: Int = 0,
    val totalClicks: Int = 0,
    val startTime: Long = 0,
    val pauseTime: Long = 0
)
