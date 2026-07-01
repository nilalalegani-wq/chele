package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ChelehDay
import com.example.data.ChelehRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChelehViewModel(private val repository: ChelehRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.ensureDaysExist()
        }
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

class ChelehViewModelFactory(private val repository: ChelehRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChelehViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChelehViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
