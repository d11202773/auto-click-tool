package com.autoclick.tool.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "click_points")
data class ClickPoint(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val x: Float,
    val y: Float,
    val delayMs: Long = 1000,
    val repeatCount: Int = 1,
    val isEnabled: Boolean = true,
    val order: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable
