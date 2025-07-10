package com.autoclick.tool.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoclick.tool.data.model.AppSettings
import com.autoclick.tool.data.model.AutoClickSession
import com.autoclick.tool.data.model.ClickPoint
import com.autoclick.tool.data.repository.ClickPointRepository
import com.autoclick.tool.data.repository.SettingsRepository
import com.autoclick.tool.service.AutoClickAccessibilityService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val clickPointRepository: ClickPointRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    val clickPoints = clickPointRepository.getAllClickPoints()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val settings = settingsRepository.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )
    
    init {
        // Monitor service status
        viewModelScope.launch {
            while (true) {
                updateServiceStatus()
                kotlinx.coroutines.delay(1000) // Update every second
            }
        }
    }
    
    fun addClickPoint(clickPoint: ClickPoint) {
        viewModelScope.launch {
            try {
                clickPointRepository.insertClickPoint(clickPoint)
                _uiState.value = _uiState.value.copy(
                    message = "Đã thêm điểm nhấp"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Lỗi khi thêm điểm nhấp: ${e.message}"
                )
            }
        }
    }
    
    fun updateClickPoint(clickPoint: ClickPoint) {
        viewModelScope.launch {
            try {
                clickPointRepository.updateClickPoint(clickPoint)
                _uiState.value = _uiState.value.copy(
                    message = "Đã cập nhật điểm nhấp"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Lỗi khi cập nhật điểm nhấp: ${e.message}"
                )
            }
        }
    }
    
    fun deleteClickPoint(clickPoint: ClickPoint) {
        viewModelScope.launch {
            try {
                clickPointRepository.deleteClickPoint(clickPoint)
                _uiState.value = _uiState.value.copy(
                    message = "Đã xóa điểm nhấp"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Lỗi khi xóa điểm nhấp: ${e.message}"
                )
            }
        }
    }
    
    fun toggleClickPointEnabled(id: Long, enabled: Boolean) {
        viewModelScope.launch {
            clickPointRepository.updateClickPointEnabled(id, enabled)
        }
    }
    
    fun startAutoClick() {
        viewModelScope.launch {
            val service = AutoClickAccessibilityService.getInstance()
            if (service != null) {
                val enabledPoints = clickPointRepository.getEnabledClickPoints().first()
                if (enabledPoints.isNotEmpty()) {
                    service.startAutoClick(enabledPoints)
                    _uiState.value = _uiState.value.copy(
                        message = "Đã bắt đầu auto click"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Không có điểm nhấp nào được kích hoạt"
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    error = "Dịch vụ accessibility chưa được kích hoạt"
                )
            }
        }
    }
    
    fun stopAutoClick() {
        AutoClickAccessibilityService.getInstance()?.stopAutoClick()
        _uiState.value = _uiState.value.copy(
            message = "Đã dừng auto click"
        )
    }
    
    fun pauseAutoClick() {
        AutoClickAccessibilityService.getInstance()?.pauseAutoClick()
        _uiState.value = _uiState.value.copy(
            message = "Đã tạm dừng auto click"
        )
    }
    
    fun resumeAutoClick() {
        AutoClickAccessibilityService.getInstance()?.resumeAutoClick()
        _uiState.value = _uiState.value.copy(
            message = "Đã tiếp tục auto click"
        )
    }
    
    fun updateSettings(settings: AppSettings) {
        viewModelScope.launch {
            settingsRepository.updateSettings(settings)
        }
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    private fun updateServiceStatus() {
        val service = AutoClickAccessibilityService.getInstance()
        val isServiceRunning = AutoClickAccessibilityService.isServiceRunning()
        val isAutoClickRunning = service?.isRunning() ?: false
        val currentSession = service?.getCurrentSession()
        
        _uiState.value = _uiState.value.copy(
            isServiceRunning = isServiceRunning,
            isAutoClickRunning = isAutoClickRunning,
            currentSession = currentSession
        )
    }
    
    fun deleteAllClickPoints() {
        viewModelScope.launch {
            try {
                clickPointRepository.deleteAllClickPoints()
                _uiState.value = _uiState.value.copy(
                    message = "Đã xóa tất cả điểm nhấp"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Lỗi khi xóa điểm nhấp: ${e.message}"
                )
            }
        }
    }
}

data class MainUiState(
    val isServiceRunning: Boolean = false,
    val isAutoClickRunning: Boolean = false,
    val currentSession: AutoClickSession? = null,
    val message: String? = null,
    val error: String? = null
)
