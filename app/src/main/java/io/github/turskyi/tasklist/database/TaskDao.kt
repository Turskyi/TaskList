package io.github.turskyi.tasklist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.turskyi.tasklist.database.TaskEntry.Companion.TABLE_TASKS

@Dao
interface TaskDao {
    @Query("SELECT * FROM $TABLE_TASKS ORDER BY priority")
    fun loadAllTasks(): LiveData<List<TaskEntry>?>?

    @Insert
    fun insertTask(taskEntry: TaskEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(taskEntry: TaskEntry)

    @Delete
    fun deleteTask(taskEntry: TaskEntry)

    @Query("SELECT * FROM $TABLE_TASKS WHERE id = :id")
    fun loadTaskById(id: Int): LiveData<TaskEntry?>?
}
