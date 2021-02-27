package io.github.turskyi.tasklist.presentation.features.addtask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.turskyi.tasklist.database.AppDatabase
import io.github.turskyi.tasklist.database.TaskEntry

class AddTaskViewModel(database: AppDatabase, taskId: Int) : ViewModel() {

    val task: LiveData<TaskEntry?>? = database.taskDao()?.loadTaskById(taskId)

}