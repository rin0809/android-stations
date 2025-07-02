package com.example.techtrain.railway.android

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.techtrain.railway.android.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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
            .baseUrl("https://railway.bookreview.techtrain.dev/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val booksService = retrofit.create(BooksService::class.java)

        binding.button.setOnClickListener {
            booksService.publicBooks().enqueue(object : Callback<List<Book>> {
                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    if (response.isSuccessful) {
                        val books = response.body()
                        val displayText = books?.joinToString("\n") ?: "No books"
                        binding.text.text = displayText
                    } else {
                        binding.text.text = "Error: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                    binding.text.text = "Failed: ${t.message}"
                }
            })
        }





    }

    // ライフサイクルログ（おまけ）
    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
}

