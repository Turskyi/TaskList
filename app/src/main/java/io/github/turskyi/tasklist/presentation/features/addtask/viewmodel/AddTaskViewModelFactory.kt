package io.github.turskyi.tasklist.presentation.features.addtask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import io.github.turskyi.tasklist.database.AppDatabase

class AddTaskViewModelFactory(private val mDb: AppDatabase, private val mTaskId: Int) :
    NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AddTaskViewModel(mDb, mTaskId) as T
    }
}