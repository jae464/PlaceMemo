package com.jae464.presentation.extension

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImage(filePath: String) {
    Glide.with(this.context)
        .load(filePath)
        .override(256,256)
        .into(this)
}