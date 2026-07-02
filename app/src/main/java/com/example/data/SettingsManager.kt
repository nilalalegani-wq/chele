package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cheleh_settings", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(getThemeMode())
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _colorTheme = MutableStateFlow(getColorTheme())
    val colorTheme: StateFlow<String> = _colorTheme.asStateFlow()

    private val _vibrationEnabled = MutableStateFlow(getVibrationEnabled())
    val vibrationEnabled: StateFlow<Boolean> = _vibrationEnabled.asStateFlow()

    private val _dailyNotificationEnabled = MutableStateFlow(getDailyNotificationEnabled())
    val dailyNotificationEnabled: StateFlow<Boolean> = _dailyNotificationEnabled.asStateFlow()

    private val _dailyNotificationTime = MutableStateFlow(getDailyNotificationTime())
    val dailyNotificationTime: StateFlow<String> = _dailyNotificationTime.asStateFlow()

    private val _charityNotificationEnabled = MutableStateFlow(getCharityNotificationEnabled())
    val charityNotificationEnabled: StateFlow<Boolean> = _charityNotificationEnabled.asStateFlow()

    fun getThemeMode(): String = prefs.getString("theme_mode", "SYSTEM") ?: "SYSTEM"
    fun setThemeMode(mode: String) {
        prefs.edit().putString("theme_mode", mode).apply()
        _themeMode.value = mode
    }

    fun getColorTheme(): String = prefs.getString("color_theme", "GOLD") ?: "GOLD"
    fun setColorTheme(theme: String) {
        prefs.edit().putString("color_theme", theme).apply()
        _colorTheme.value = theme
    }

    fun getVibrationEnabled(): Boolean = prefs.getBoolean("vibration_enabled", true)
    fun setVibrationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("vibration_enabled", enabled).apply()
        _vibrationEnabled.value = enabled
    }

    fun getDailyNotificationEnabled(): Boolean = prefs.getBoolean("daily_notification_enabled", true)
    fun setDailyNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("daily_notification_enabled", enabled).apply()
        _dailyNotificationEnabled.value = enabled
    }

    fun getDailyNotificationTime(): String = prefs.getString("daily_notification_time", "20:00") ?: "20:00"
    fun setDailyNotificationTime(time: String) {
        prefs.edit().putString("daily_notification_time", time).apply()
        _dailyNotificationTime.value = time
    }

    fun getCharityNotificationEnabled(): Boolean = prefs.getBoolean("charity_notification_enabled", true)
    fun setCharityNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("charity_notification_enabled", enabled).apply()
        _charityNotificationEnabled.value = enabled
    }
}
