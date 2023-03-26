package pl.ar97.workmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application):AndroidViewModel(application) {
    private val rep = Repository(application)

    fun startTasks(){
        rep.enqueueTasksChain()
    }
}