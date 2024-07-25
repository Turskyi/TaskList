package io.github.turskyi.tasklist.presentation.features.main.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.turskyi.tasklist.AppExecutors
import io.github.turskyi.tasklist.R
import io.github.turskyi.tasklist.database.AppDatabase
import io.github.turskyi.tasklist.database.TaskEntry
import io.github.turskyi.tasklist.presentation.features.addtask.AddTaskActivity
import io.github.turskyi.tasklist.presentation.features.main.MainViewModel
import io.github.turskyi.tasklist.presentation.interfaces.ItemClickListener

class MainActivity : AppCompatActivity(), ItemClickListener {

    companion object {
        /* Constant for logging */
        private val TAG = MainActivity::class.java.simpleName
    }

    /* Member variables for the adapter and RecyclerView */
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: TaskAdapter? = null
    private var mDb: AppDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Set the RecyclerView to its corresponding view */
        mRecyclerView = findViewById(R.id.recyclerViewTasks)

        /* Set the layout for the RecyclerView to be a linear layout, which measures and
         positions items within a RecyclerView into a linear list */
        mRecyclerView?.layoutManager = LinearLayoutManager(this)

        /* Initialize the adapter and attach it to the RecyclerView */
        mAdapter = TaskAdapter(this, this)
        mRecyclerView?.adapter = mAdapter
        val decoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        mRecyclerView?.addItemDecoration(decoration)

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            /* Called when a user swipes left or right on a ViewHolder. */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                /* Implementation of swipe to delete. */
                AppExecutors.instance?.diskIO()?.execute {
                    val position = viewHolder.adapterPosition
                    val tasks: List<TaskEntry>? = mAdapter?.tasks
                    if (tasks != null) {
                        mDb?.taskDao()?.deleteTask(tasks[position])
                    }
                }
            }
        }).attachToRecyclerView(mRecyclerView)

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attaching an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        val fabButton = findViewById<FloatingActionButton>(R.id.fab)
        fabButton.setOnClickListener {
            /* Create a new intent to start an AddTaskActivity */
            val addTaskIntent = Intent(this@MainActivity, AddTaskActivity::class.java)
            startActivity(addTaskIntent)
        }
        mDb = AppDatabase.getInstance(applicationContext)
        setupViewModel()
    }

    override fun onItemClickListener(itemId: Int) {
        /* Launching AddTaskActivity adding the itemId as an extra in the intent */
        val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, itemId)
        startActivity(intent)
    }

    private fun setupViewModel() {
        val viewModel: MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.tasks?.observe(this) { taskEntries ->
            Log.d(TAG, "Updating list of tasks from LiveData in ViewModel")
            mAdapter?.tasks = taskEntries
        }
    }
}