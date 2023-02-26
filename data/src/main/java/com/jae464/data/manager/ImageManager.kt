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

    fun saveImage(memoId: Long, imagePath: String) {
        Log.d(TAG, "SAVE IMAGE START")
        Glide.with(context)
            .asBitmap()
            .load(imagePath.toUri())
            .listener(ImageRequestListener(imagePath, memoId))
            .override(100,100)
            .submit()
    }

    inner class ImageRequestListener(
        private val imagePath: String,
        private val memoId: Long
    ): RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean
        ): Boolean {
            Log.e(TAG, "이미지 저장 실패")
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
                val dirPath = File(context.filesDir, "$DIR_NAME/$memoId").apply { mkdirs() }
                val filePath = File("${dirPath}/${imagePath.substringAfterLast("/")}")
                Log.d(TAG, "이미지 저장 경로 : $filePath")
                withContext(Dispatchers.IO) {
                    FileOutputStream(filePath).use { out ->
                        resource?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }
                    Log.d(TAG, "이미지 저장 성공")
                }

            }
            return true
        }

    }

    fun getImagePathList(memoId: Long): List<String> {
        val dirPath = "${context.filesDir}/images"
        val filePath = File("$dirPath/$memoId")
        Log.d(TAG, filePath.toString())

        val fileList = filePath.listFiles()
        fileList ?: return emptyList()
        Log.d(TAG, fileList.toString())
        val filePathList = fileList.map {file ->
            file.path
        }


        return filePathList

    }

    companion object {
        private const val DIR_NAME = "images"
    }
}