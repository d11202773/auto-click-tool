package com.autoclick.tool.data.database

import androidx.room.*
import com.autoclick.tool.data.model.ClickPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface ClickPointDao {
    
    @Query("SELECT * FROM click_points ORDER BY `order` ASC, id ASC")
    fun getAllClickPoints(): Flow<List<ClickPoint>>
    
    @Query("SELECT * FROM click_points WHERE isEnabled = 1 ORDER BY `order` ASC, id ASC")
    fun getEnabledClickPoints(): Flow<List<ClickPoint>>
    
    @Query("SELECT * FROM click_points WHERE id = :id")
    suspend fun getClickPointById(id: Long): ClickPoint?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClickPoint(clickPoint: ClickPoint): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClickPoints(clickPoints: List<ClickPoint>)
    
    @Update
    suspend fun updateClickPoint(clickPoint: ClickPoint)
    
    @Delete
    suspend fun deleteClickPoint(clickPoint: ClickPoint)
    
    @Query("DELETE FROM click_points WHERE id = :id")
    suspend fun deleteClickPointById(id: Long)
    
    @Query("DELETE FROM click_points")
    suspend fun deleteAllClickPoints()
    
    @Query("UPDATE click_points SET isEnabled = :enabled WHERE id = :id")
    suspend fun updateClickPointEnabled(id: Long, enabled: Boolean)
    
    @Query("UPDATE click_points SET `order` = :order WHERE id = :id")
    suspend fun updateClickPointOrder(id: Long, order: Int)
}
