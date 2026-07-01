package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cheleh_days")
data class ChelehDay(
    @PrimaryKey val dayNumber: Int,
    val isCompleted: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    val notes: String = "",
    
    // Step 1: 100x Takbeer first
    val step1Count: Int = 0,
    
    // Step 2: 1x Salam
    val step2Completed: Boolean = false,
    
    // Step 3: 1x Curse (La'n)
    val step3Completed: Boolean = false,
    
    // Step 4: First part of Ziyarat Ashura
    val step4Completed: Boolean = false,
    
    // Step 5: 2 Rak'at prayer (Gift)
    val step5Completed: Boolean = false,
    
    // Step 6: 100x Takbeer second
    val step6Count: Int = 0,
    
    // Step 7: Ziyarat Ashura complete (with 100x La'n & 100x Salam)
    val step7Completed: Boolean = false,
    val step7LanCount: Int = 0,
    val step7SalamCount: Int = 0,
    
    // Step 8: 2 Rak'at prayer (Ziyarat Ashura prayer)
    val step8Completed: Boolean = false
) {
    // Utility to check if all steps for the day are actually done
    fun checkFullyCompleted(): Boolean {
        return step1Count >= 100 &&
               step2Completed &&
               step3Completed &&
               step4Completed &&
               step5Completed &&
               step6Count >= 100 &&
               step7Completed &&
               step8Completed
    }
}
