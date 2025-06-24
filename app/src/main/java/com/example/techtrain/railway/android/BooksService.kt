package com.example.techtrain.railway.android

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.ZoneOffset

interface BooksService {
    @GET("public/books")
    fun publicBooks(@Query("offset") offset: String): Call<List<Book>>
}
