package com.jae464.placememo.data.manager

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.res.ResourcesCompat
import java.io.File
import java.io.FileOutputStream

object ImageManager {
    const val filePath = "/data/user/0/com.jae464.placememo/memo_image/"
    const val MAX_WIDTH = 1440
    const val MAX_HEIGHT = 1050

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

    // 해당 메모의 이미지들을 가져온다
    fun loadMemoImage(memoId: Long): List<Bitmap>?{
        val path = "${filePath}/${memoId}/"
        val file = File(path)
        val imageFiles = file.listFiles() ?: return null
        val bitmapList = mutableListOf<Bitmap>()
        imageFiles.forEach {
           val bitmapImage = BitmapFactory.decodeFile(it.path)
            bitmapList.add(bitmapImage)
        }
        return bitmapList
    }

    private fun getSampleSize(uri: Uri, context: Context): Int {
        val input = context.contentResolver.openInputStream(uri)
        var sampleSize = 1

        BitmapFactory.Options().run {
            inJustDecodeBounds = false
            BitmapFactory.decodeStream(input, null, this)
            sampleSize = calculateInSampleSize(this)
        }
        input?.close()
        return sampleSize
    }
    fun resizeBitmapFromUri(uri: Uri, context: Context): Bitmap? {
        val input = context.contentResolver.openInputStream(uri)
        var bitmap: Bitmap?
        val sampleSize = getSampleSize(uri, context)

        BitmapFactory.Options().run {
            inJustDecodeBounds = false
            inSampleSize = sampleSize
            bitmap = BitmapFactory.decodeStream(input, null, this)
            println(bitmap?.density)
        }
        input?.close()
        return bitmap
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        var height = options.outHeight
        var width = options.outWidth

        println("height : $height width : $width")
        var inSampleSize = 1

        while(height > MAX_HEIGHT || width > MAX_WIDTH) {
            height /= 2
            width /= 2
            inSampleSize *= 2
        }

        return inSampleSize
    }

    fun changeColor(color: Int, resourceId: Int, context: Context): Bitmap {
        val drawable = ResourcesCompat.getDrawable(context.resources, resourceId, null)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable!!.intrinsicWidth,
            Bitmap.Config.ARGB_8888
        )
        val paint = Paint()
        val colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        paint.colorFilter = colorFilter

        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bitmap, 0f,0f,paint)

        return bitmap

    }

}