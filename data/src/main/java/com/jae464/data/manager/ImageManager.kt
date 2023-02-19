package com.jae464.data.manager

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ImageManager @Inject constructor(
    private val context: Context
) {
    private val TAG = "ImageManager"
    fun saveImage(imagePath: String) {
        Log.d(TAG, "SAVE IMAGE START")
        Glide.with(context)
            .asBitmap()
            .load(imagePath.toUri())
            .listener(ImageRequestListener(imagePath))
            .override(100,100)
            .submit()
    }

    inner class ImageRequestListener(
        private val imagePath: String
    ): RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean
        ): Boolean {
            Log.e(TAG, "Failed To Save Image")
            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            CoroutineScope(Dispatchers.IO).launch {
                val dirPath = File(context.filesDir, DIR_NAME).apply { mkdirs() }
                val filePath = File("${dirPath}/${imagePath.substringAfterLast("/")}")
                Log.d(TAG, filePath.toString())
                withContext(Dispatchers.IO) {
                    FileOutputStream(filePath).use { out ->
                        resource?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }
                }

            }
            return true
        }

    }

    companion object {
        private const val DIR_NAME = "images"
    }
}