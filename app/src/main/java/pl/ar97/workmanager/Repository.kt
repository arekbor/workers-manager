package pl.ar97.workmanager

import android.content.Context
import androidx.work.*
import pl.ar97.workmanager.data.workers.BITMAP_FILENAME
import pl.ar97.workmanager.data.workers.ClearCache
import pl.ar97.workmanager.data.workers.ImageOperationWorker
import pl.ar97.workmanager.data.workers.SendImageToServerWorker

class Repository(context: Context) {
    private val workManager = WorkManager.getInstance(context)
    private val path = context.getExternalFilesDir(null)?.path + "/$BITMAP_FILENAME"

    fun enqueueTasksChain(){
        val networkConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val firstTask = OneTimeWorkRequestBuilder<ImageOperationWorker>()
            .setConstraints(networkConstraints)
            .build()

        val secondTask = OneTimeWorkRequestBuilder<SendImageToServerWorker>()
            .setConstraints(networkConstraints)
            .build()

        val inputData = Data.Builder().putString("PATH", path).build()

        val thirdTask = OneTimeWorkRequestBuilder<ClearCache>()
            .setInputData(inputData)
            .build()

        workManager.beginUniqueWork("IMAGE_OPERATION_CHAIN", ExistingWorkPolicy.REPLACE, firstTask)
            .then(secondTask)
            .then(thirdTask)
            .enqueue()
    }
}