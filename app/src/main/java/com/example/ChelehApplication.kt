package com.example

import android.app.Application
import com.example.data.ChelehDatabase
import com.example.data.ChelehRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ChelehApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ChelehDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ChelehRepository(database.chelehDao()) }
}
