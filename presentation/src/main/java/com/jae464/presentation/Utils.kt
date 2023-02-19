package com.jae464.presentation

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

fun indexToCategory(index: Int): com.jae464.domain.model.post.Category =
    when(index) {
        0 -> com.jae464.domain.model.post.Category.RESTAURANT
        1 -> com.jae464.domain.model.post.Category.TOURIST
        2 -> com.jae464.domain.model.post.Category.CAFE
        3 -> com.jae464.domain.model.post.Category.HOTEL
        4 -> com.jae464.domain.model.post.Category.OTHER
        else -> com.jae464.domain.model.post.Category.OTHER
    }

