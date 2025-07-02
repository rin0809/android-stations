package com.example.techtrain.railway.android

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface BooksService {
    @GET("public/books")
    fun publicBooks(@Query("offset") offset: String = "0"): Call<List<Book>>
}



