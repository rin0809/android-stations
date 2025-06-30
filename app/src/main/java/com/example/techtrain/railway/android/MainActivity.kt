package com.example.techtrain.railway.android

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
            .baseUrl("http://railway.bookreview.techtrain.dev/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val booksService = retrofit.create(BooksService::class.java)

        binding.button.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val books = withContext(Dispatchers.IO) {
                        booksService.publicBooks("0")
                    }

                    val displayText = if (books.isNotEmpty()) books[0].toString() else "No books"
                    binding.text.text = displayText

                    // TextView の内容を正規表現で解析（ボタンクリック後に行う）
                    val regex = Regex("""Book\(id=([^,]+), title=([^,]+), url=([^,]+), detail=([^,]+), review=([^,]+), reviewer=([^\)]+)\)""")
                    val matchResults = regex.findAll(binding.text.text).toList()
                    if (matchResults.isNotEmpty()) {
                        val parsedBooks = matchResults.map { match ->
                            Book(
                                id = match.groupValues[1],
                                title = match.groupValues[2],
                                url = match.groupValues[3],
                                detail = match.groupValues[4],
                                review = match.groupValues[5],
                                reviewer = match.groupValues[6]
                            )
                        }
                        // ここで parsedBooks を使って何か表示したりログ出力したりできます
                        Log.d(TAG, "Parsed books: $parsedBooks")
                    }

                } catch (e: Exception) {
                    binding.text.text = "Failed: ${e.message}"
                }
            }
        }
    }

    // ライフサイクルログ（おまけ）
    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
}

