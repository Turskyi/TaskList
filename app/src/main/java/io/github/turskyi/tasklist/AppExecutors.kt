package io.github.turskyi.tasklist

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Global executor pools for the whole application.
 *
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
class AppExecutors private constructor(
    private val diskIO: Executor,
    private val networkIO: Executor,
    private val mainThread: Executor,
) {
    companion object {
        /* For Singleton instantiation */
        private val LOCK = Any()
        private var sInstance: AppExecutors? = null
        val instance: AppExecutors?
            get() {
                if (sInstance == null) {
                    synchronized(LOCK) {
                        sInstance = AppExecutors(
                            diskIO = Executors.newSingleThreadExecutor(),
                            networkIO = Executors.newFixedThreadPool(3),
                            mainThread = MainThreadExecutor()
                        )
                    }
                }
                return sInstance
            }
    }

    /** ensures than all tasks will be executed in order and in background thread */
    fun diskIO(): Executor {
        return diskIO
    }

    /** this can be used when not an activity requires an access to ui thread */
    fun mainThread(): Executor {
        return mainThread
    }

    fun networkIO(): Executor {
        return networkIO
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}