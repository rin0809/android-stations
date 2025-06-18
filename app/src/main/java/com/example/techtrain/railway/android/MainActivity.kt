package com.example.techtrain.railway.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityMainBinding
import android.content.Intent
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding  // バインディング変数
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)  // バインディング初期化
        setContentView(binding.root)

        binding.text.text = "Hello Railway!"

        binding.button.setOnClickListener {
            // ここにAPI呼び出し処理を入れる

            // 1. RetrofitのService呼び出し
            val call = booksService.getBooks()

            // 2. enqueueで非同期通信
            call.enqueue(object : Callback<List<Book>> {
                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    if (response.isSuccessful) {
                        val books = response.body()
                        // List<Book>をtoString()で文字列化しTextViewに表示
                        binding.text.text = books?.toString() ?: "No books found"
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

    val retrofit = Retrofit.Builder()
        .baseUrl("http://railway.bookreview.techtrain.dev/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val booksService = retrofit.create(BooksService::class.java)

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
