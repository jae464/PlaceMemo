package com.jae464.presentation.extension

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2

@BindingAdapter("bindImage")
fun bindImage(view: ImageView, filePath: String) {
    bindImageWithSize(view, filePath, 100, 100)
//    Glide.with(view.context)
//        .
}

@BindingAdapter("submitImagePathList")
fun bindSubmitImagePathList(view: ViewPager2, imagePathList: List<String>) {
    view.adapter?.let {

    }
}

fun bindImageWithSize(view: ImageView, filePath: String, width: Int, height: Int) {
    Log.d("BindingAdapter", filePath)
}

@BindingAdapter("visibility")
fun bindVisibility(view: View, visibility: Boolean) {
    view.visibility = if(visibility) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
