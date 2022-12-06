package com.jae464.placememo.presentation

import com.jae464.placememo.domain.model.post.Category

fun regionToString(area1: String, area2: String, area3: String): String {
    return "$area1 $area2 $area3"
}

fun categoryToString(code: Int): String =
    when(code) {
        0 -> "음식점"
        1 -> "관광지"
        2 -> "카페"
        3 -> "호텔"
        4 -> "기타"
        else -> ""
    }

fun indexToCategory(index: Int): Category =
    when(index) {
        0 -> Category.RESTAURANT
        1 -> Category.TOURIST
        2 -> Category.CAFE
        3 -> Category.HOTEL
        4 -> Category.OTHER
        else -> Category.OTHER
    }
