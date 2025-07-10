package com.autoclick.tool.data.repository

import com.autoclick.tool.data.database.ClickPointDao
import com.autoclick.tool.data.model.ClickPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClickPointRepository @Inject constructor(
    private val clickPointDao: ClickPointDao
) {
    
    fun getAllClickPoints(): Flow<List<ClickPoint>> {
        return clickPointDao.getAllClickPoints()
    }
    
    fun getEnabledClickPoints(): Flow<List<ClickPoint>> {
        return clickPointDao.getEnabledClickPoints()
    }
    
    suspend fun getClickPointById(id: Long): ClickPoint? {
        return clickPointDao.getClickPointById(id)
    }
    
    suspend fun insertClickPoint(clickPoint: ClickPoint): Long {
        return clickPointDao.insertClickPoint(clickPoint)
    }
    
    suspend fun updateClickPoint(clickPoint: ClickPoint) {
        clickPointDao.updateClickPoint(clickPoint.copy(updatedAt = System.currentTimeMillis()))
    }
    
    suspend fun deleteClickPoint(clickPoint: ClickPoint) {
        clickPointDao.deleteClickPoint(clickPoint)
    }
    
    suspend fun deleteClickPointById(id: Long) {
        clickPointDao.deleteClickPointById(id)
    }
    
    suspend fun updateClickPointEnabled(id: Long, enabled: Boolean) {
        clickPointDao.updateClickPointEnabled(id, enabled)
    }
    
    suspend fun reorderClickPoints(clickPoints: List<ClickPoint>) {
        clickPoints.forEachIndexed { index, clickPoint ->
            clickPointDao.updateClickPointOrder(clickPoint.id, index)
        }
    }
    
    suspend fun deleteAllClickPoints() {
        clickPointDao.deleteAllClickPoints()
    }
}
