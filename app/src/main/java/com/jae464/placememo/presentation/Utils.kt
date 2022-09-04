package com.jae464.placememo.presentation

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

