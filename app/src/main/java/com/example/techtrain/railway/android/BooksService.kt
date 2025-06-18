package com.example.techtrain.railway.android

import retrofit2.Call
import retrofit2.http.GET

interface BooksService {
    @GET("public/books")
    fun getBooks(): Call<List<Book>>
}
