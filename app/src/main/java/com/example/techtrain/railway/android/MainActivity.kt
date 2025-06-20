package com.example.techtrain.railway.android

import kotlinx.coroutines.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityMainBinding
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

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
            // ワーカースレッドで非同期処理
            GlobalScope.launch(Dispatchers.IO) {
                val call = booksService.publicBooks("0")
                call.enqueue(object : Callback<List<Book>> {
                    override fun onResponse(
                        call: Call<List<Book>>,
                        response: Response<List<Book>>
                    ) {
                        val booksText = response.body()?.toString() ?: "No books"
                        // UI更新はメインスレッドで
                        runOnUiThread {
                            binding.text.text = booksText
                        }
                    }

                    override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                        runOnUiThread {
                            binding.text.text = "Failed: ${t.message}"
                        }
                    }
                })
            }
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
