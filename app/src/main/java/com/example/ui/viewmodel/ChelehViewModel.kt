package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ChelehDay
import com.example.data.ChelehRepository
import com.example.data.SettingsManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChelehViewModel(
    private val repository: ChelehRepository,
    val settingsManager: SettingsManager
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.ensureDaysExist()
        }
    }

    val themeMode = settingsManager.themeMode
    val colorTheme = settingsManager.colorTheme
    val vibrationEnabled = settingsManager.vibrationEnabled
    val dailyNotificationEnabled = settingsManager.dailyNotificationEnabled
    val dailyNotificationTime = settingsManager.dailyNotificationTime
    val charityNotificationEnabled = settingsManager.charityNotificationEnabled

    fun setThemeMode(mode: String) {
        settingsManager.setThemeMode(mode)
    }

    fun setColorTheme(theme: String) {
        settingsManager.setColorTheme(theme)
    }

    fun setVibrationEnabled(enabled: Boolean) {
        settingsManager.setVibrationEnabled(enabled)
    }

    fun setDailyNotificationEnabled(enabled: Boolean) {
        settingsManager.setDailyNotificationEnabled(enabled)
    }

    fun setDailyNotificationTime(time: String) {
        settingsManager.setDailyNotificationTime(time)
    }

    fun setCharityNotificationEnabled(enabled: Boolean) {
        settingsManager.setCharityNotificationEnabled(enabled)
    }

    val allDays: StateFlow<List<ChelehDay>> = repository.allDays
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedDayNumber = MutableStateFlow<Int?>(null)
    val selectedDayNumber: StateFlow<Int?> = _selectedDayNumber.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeDay: StateFlow<ChelehDay?> = _selectedDayNumber
        .flatMapLatest { dayNum ->
            if (dayNum != null) {
                repository.getDayByNumber(dayNum)
            } else {
                MutableStateFlow(null)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun selectDay(dayNumber: Int) {
        _selectedDayNumber.value = dayNumber
    }

    fun clearSelectedDay() {
        _selectedDayNumber.value = null
    }

    fun updateDay(day: ChelehDay) {
        viewModelScope.launch {
            repository.updateDay(day)
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllDays()
        }
    }
}

class ChelehViewModelFactory(
    private val repository: ChelehRepository,
    private val settingsManager: SettingsManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChelehViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChelehViewModel(repository, settingsManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
