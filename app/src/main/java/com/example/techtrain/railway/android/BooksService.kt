package com.example.techtrain.railway.android

import retrofit2.http.GET
import retrofit2.http.Query


interface BooksService {
    @GET("public/books")
    suspend fun publicBooks(@Query("offset") offset: String): List<Book>
}
