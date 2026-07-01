package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ChelehRepository(private val chelehDao: ChelehDao) {
    val allDays: Flow<List<ChelehDay>> = chelehDao.getAllDays()

    fun getDayByNumber(dayNumber: Int): Flow<ChelehDay?> {
        return chelehDao.getDayByNumber(dayNumber)
    }

    suspend fun updateDay(day: ChelehDay) {
        // Automatically check if the day is fully completed and set the flag
        val updatedDay = day.copy(
            isCompleted = day.checkFullyCompleted(),
            lastUpdated = System.currentTimeMillis()
        )
        chelehDao.updateDay(updatedDay)
    }

    suspend fun resetAllDays() {
        chelehDao.resetAllDays()
    }

    suspend fun ensureDaysExist() {
        val days = chelehDao.getAllDays().first()
        if (days.size < 40) {
            val missingDays = (1..40).map { dayNum ->
                chelehDao.getDayByNumberSync(dayNum) ?: ChelehDay(dayNumber = dayNum)
            }
            chelehDao.insertAllDays(missingDays)
        }
    }
}
