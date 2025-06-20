package com.example.techtrain.railway.android

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Book(
    val id: String,
    val title: String,
    val url: String,
    val detail: String,
    val review: String,
    val reviewer: String
)
