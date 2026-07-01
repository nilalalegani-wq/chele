package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChelehDao {
    @Query("SELECT * FROM cheleh_days ORDER BY dayNumber ASC")
    fun getAllDays(): Flow<List<ChelehDay>>

    @Query("SELECT * FROM cheleh_days WHERE dayNumber = :dayNumber LIMIT 1")
    fun getDayByNumber(dayNumber: Int): Flow<ChelehDay?>

    @Query("SELECT * FROM cheleh_days WHERE dayNumber = :dayNumber LIMIT 1")
    suspend fun getDayByNumberSync(dayNumber: Int): ChelehDay?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: ChelehDay)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDays(days: List<ChelehDay>)

    @Update
    suspend fun updateDay(day: ChelehDay)

    @Query("UPDATE cheleh_days SET isCompleted = 0, step1Count = 0, step2Completed = 0, step3Completed = 0, step4Completed = 0, step5Completed = 0, step6Count = 0, step7Completed = 0, step7LanCount = 0, step7SalamCount = 0, step8Completed = 0, notes = ''")
    suspend fun resetAllDays()
}
