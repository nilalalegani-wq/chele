package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ChelehDay::class], version = 1, exportSchema = false)
abstract class ChelehDatabase : RoomDatabase() {
    abstract fun chelehDao(): ChelehDao

    companion object {
        @Volatile
        private var INSTANCE: ChelehDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ChelehDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChelehDatabase::class.java,
                    "cheleh_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        scope.launch(Dispatchers.IO) {
                            val dao = INSTANCE?.chelehDao()
                            if (dao != null) {
                                val initialDays = (1..40).map { dayNum ->
                                    ChelehDay(dayNumber = dayNum)
                                }
                                dao.insertAllDays(initialDays)
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
