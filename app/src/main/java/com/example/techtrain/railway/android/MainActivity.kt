package com.example.techtrain.railway.android

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var booksService: BooksService

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://railway.bookreview.techtrain.dev/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        booksService = retrofit.create(BooksService::class.java)

        binding.button.setOnClickListener {
            Log.d(TAG, "Button clicked - start")
            booksService.publicBooks().enqueue(object : Callback<List<Book>> {
                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    Log.d(TAG, "Response received")
                    if (response.isSuccessful) {
                        val books = response.body()
                        val displayText = books?.joinToString("\n") ?: "No books"
                        binding.text.text = displayText
                    } else {
                        binding.text.text = "Error: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                    Log.d(TAG, "Request failed: ${t.message}")
                    binding.text.text = "Failed: ${t.message}"
                }
            })
            Log.d(TAG, "Button clicked - end")
        }
    }
}
