package io.github.turskyi.tasklist.database

import androidx.room.*
import io.github.turskyi.tasklist.database.TaskEntry.Companion.TABLE_TASKS
import java.util.*

@Entity(tableName = TABLE_TASKS)
data class TaskEntry(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var description: String,
    var priority: Int,
    @ColumnInfo(name = COLUMN_UPDATED_AT) var updatedAt: Date,
) {
    @Ignore
    constructor(description: String, priority: Int, updatedAt: Date) : this(
        0,
        description = description,
        priority = priority,
        updatedAt = updatedAt,
    ) {
        this.description = description
        this.priority = priority
        this.updatedAt = updatedAt
    }

    companion object {
        const val TABLE_TASKS = "task"
        const val COLUMN_UPDATED_AT = "updated_at"
    }
}