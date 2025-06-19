package com.example.techtrain.railway.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.techtrain.railway.android.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    // RetrofitとBooksServiceは一度だけ生成
    private val booksService: BooksService by lazy {
        // テスト用のモックがIntentに入っていれば使う。なければデフォルト生成
        (intent.getSerializableExtra("MOCK_SERVICE") as? BooksService) ?: createDefaultService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.text.text = "Hello Railway!"

        binding.button.setOnClickListener {
            val inputText = binding.editText.text.toString()
            binding.text.text = inputText

            // SecondActivityへ遷移
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra("KEY_INPUT_TEXT", inputText)
            }
            startActivity(intent)

            // Retrofitの非同期API呼び出し
            booksService.publicBooks("0").enqueue(object : Callback<List<Book>> {
                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    if (response.isSuccessful) {
                        val books = response.body()
                        binding.text.text = books?.toString() ?: "No books found"
                        Log.d(TAG, "Books fetched successfully: $books")
                    } else {
                        val errorMsg = "Error: ${response.code()} ${response.message()}"
                        binding.text.text = errorMsg
                        Log.e(TAG, errorMsg)
                    }
                }

                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                    val failMsg = "Failed: ${t.localizedMessage ?: t.message}"
                    binding.text.text = failMsg
                    Log.e(TAG, failMsg, t)
                }
            })
        }
    }

    private fun createDefaultService(): BooksService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://railway.bookreview.techtrain.dev/") // 可能ならhttpsに変更推奨
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(BooksService::class.java)
    }

    // ライフサイクルログ（任意）
    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
}
