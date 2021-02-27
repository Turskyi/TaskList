package io.github.turskyi.tasklist.presentation.features.main.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.github.turskyi.tasklist.R
import io.github.turskyi.tasklist.database.TaskEntry
import io.github.turskyi.tasklist.presentation.interfaces.ItemClickListener
import java.text.SimpleDateFormat
import java.util.*

/**
 * This TaskAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
class TaskAdapter(
    private val mContext: Context,
    private val mItemClickListener: ItemClickListener,
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    companion object {
        /* Constant for date format */
        private const val DATE_FORMAT = "dd/MM/yyy"
    }

    /* Class variables for the List that holds task data and the Context */
    private var mTaskEntries: List<TaskEntry>? = null

    /* Date formatter */
    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        /* Inflate the task_layout to a view */
        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.task_layout, parent, false)
        return TaskViewHolder(view)
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        /* Determine the values of the wanted data */
        if (mTaskEntries != null) {
            val (_, description, priority, updatedAt) = mTaskEntries!![position]
            val mUpdatedAt = dateFormat.format(updatedAt)

            /* Set values */
            holder.taskDescriptionView.text = description
            holder.updatedAtView.text = mUpdatedAt

            /* Programmatically set the text and color for the priority TextView */
            val priorityString = "" + priority // converts int to String
            holder.priorityView.text = priorityString

            val priorityCircle = holder.priorityView.background as GradientDrawable
            /* Get the appropriate background color based on the priority */
            val priorityColor = getPriorityColor(priority)
            priorityCircle.setColor(priorityColor)
        }
    }

    /*
    Helper method for selecting the correct priority circle color.
    P1 = red, P2 = orange, P3 = yellow
    */
    private fun getPriorityColor(priority: Int): Int {
        var priorityColor = 0
        when (priority) {
            1 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialRed)
            2 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange)
            3 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow)
        }
        return priorityColor
    }

    /**
     * Returns the number of items to display.
     */
    override fun getItemCount(): Int = if (mTaskEntries == null) 0 else mTaskEntries!!.size

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    var tasks: List<TaskEntry>?
        get() = mTaskEntries
        set(taskEntries) {
            mTaskEntries = taskEntries
            notifyDataSetChanged()
        }

    /** Inner class for creating ViewHolders */
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        /* Class variables for the task description and priority TextViews */
        var taskDescriptionView: TextView = itemView.findViewById(R.id.taskDescription)
        var updatedAtView: TextView = itemView.findViewById(R.id.taskUpdatedAt)
        var priorityView: TextView = itemView.findViewById(R.id.priorityTextView)

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mTaskEntries != null) {
                val elementId = mTaskEntries!![adapterPosition].id
                mItemClickListener.onItemClickListener(elementId)
            }
        }
    }
}