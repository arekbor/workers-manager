package pl.ar97.workmanager.data.workers

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

const val BITMAP_FILENAME = "temp.jpg"

class ImageOperationWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
        val bitmap = applicationContext
            .resources
            .getDrawable(
                androidx.core.R.drawable.notification_bg,
                applicationContext.theme)
            .toBitmap()

        val scaleBitmap = bitmap.scale(
            width = bitmap.width*5,
            height = bitmap.height * 5)

        return when(saveImage(scaleBitmap)){
            true -> {
                Log.d("BITMAP_D", "Bitmap scaled!")
                Result.success()
            }
            false -> {
                Log.d("BITMAP_D", "Bitmap failed!")
                Result.failure()
            }
        }
    }

    private fun saveImage(bitmap: Bitmap):Boolean{
        val path = applicationContext.getExternalFilesDir(null)?.path
        return try {
            FileOutputStream("$path/$BITMAP_FILENAME").use {out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
            true
        }catch(e: Exception){
            false
        }
    }
}

class SendImageToServerWorker(context: Context, params: WorkerParameters):Worker(context, params){
    override fun doWork(): Result {
        return when(Random.nextBoolean()){
            true -> {
                Log.d("BITMAP_D", "bitmap send success!")
                Result.success()
            }
            false -> {
                Log.d("BITMAP_D", "bitmap send faild!")
                Result.failure()
            }
        }
    }
}

class ClearCache(context: Context, private val params: WorkerParameters): Worker(context,params){
    override fun doWork(): Result {
        val path = params.inputData.getString("PATH") ?: return Result.failure()
        Log.d("BITMAP_D", "Input data: $path")

        return when(deleteCache(path = path)){
            true -> {
                Result.success()
            }
            false -> {
                Result.failure()
            }
        }
    }

    private fun deleteCache(path: String):Boolean{
        val file = File(path)
        if(file.exists()){
            return file.delete()
        }
        return false
    }
}