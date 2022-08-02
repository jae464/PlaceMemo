package com.jae464.placememo.data.manager

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

object ImageManager {
    const val filePath = "/data/user/0/com.jae464.placememo/memo_image/"
    fun saveImage(imageList: List<Bitmap>, memoId: Long) {
        val path = "${filePath}/${memoId}/"
        val file = File(path)
        if (file.exists()) file.deleteRecursively()
        file.mkdirs()
        imageList.forEachIndexed { index, bitmap ->
            val newFile = File(path, "${index}.jpg").apply {
                createNewFile()
            }
            val outputFile = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputFile)
            outputFile.close()
        }
    }

}