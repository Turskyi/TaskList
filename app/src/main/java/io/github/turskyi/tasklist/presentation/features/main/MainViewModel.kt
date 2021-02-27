package io.github.turskyi.tasklist.presentation.features.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.turskyi.tasklist.database.AppDatabase
import io.github.turskyi.tasklist.database.TaskEntry

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val tasks: LiveData<List<TaskEntry>?>?

    companion object {
        /* Constant for logging */
        private val TAG = MainViewModel::class.java.simpleName
    }

    init {
        val database = AppDatabase.getInstance(getApplication())
        Log.d(TAG, "Actively retrieving the tasks from the DataBase")
        tasks = database.taskDao()?.loadAllTasks()
    }
}