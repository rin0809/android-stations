package com.example.techtrain.railway.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // ← これが必要
import com.example.techtrain.railway.android.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import androidx.lifecycle.lifecycleScope


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.text.text = "Hello Railway!"

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://railway.bookreview.techtrain.dev/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val booksService = retrofit.create(BooksService::class.java)

        binding.button.setOnClickListener {
            val call = booksService.publicBooks("0")
            call.enqueue(object : Callback<List<Book>> {
                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    val books = response.body()
                    val displayText = if (books.isNullOrEmpty()) "No books" else books[0].toString()
                    binding.text.text = displayText
                }

                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                    binding.text.text = "Failed: ${t.message}"
                }
            })
        }


    }

    // ライフサイクルログ（おまけ）
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

}
