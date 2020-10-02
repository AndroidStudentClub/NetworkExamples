package ru.mikhailskiy.retrofitexample

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.retrofitexample.adapters.MoviesAdapter
import ru.mikhailskiy.retrofitexample.data.Movie
import ru.mikhailskiy.retrofitexample.data.MoviesResponse
import ru.mikhailskiy.retrofitexample.network.CacheApiClient
import ru.mikhailskiy.retrofitexample.network.MovieApiClient
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Шаг 1: Создать или получить мастер-ключ для шифрования / дешифрования
        val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        // Шаг 2. Инициализируйте / создайте экземпляр EncryptedSharedPreferences
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            this,
            "SHARED_PREF_NAME",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val editor = sharedPreferences.edit()
        editor.putString("KEY", API_KEY)
        editor.apply()

        search_toolbar
            .onTextChangedObservable
            .map { it.trim() }
            .doOnNext { Log.d("THR# 38", Thread.currentThread().name) }
            .debounce(500, TimeUnit.MILLISECONDS)
            .doOnNext { Log.d("THR# 40", Thread.currentThread().name) }
            .filter { it.isNotEmpty() }
            .doOnNext { Log.d("THR# 42", Thread.currentThread().name) }
            .observeOn(Schedulers.io())
            .doOnNext { Log.d("THR# 44", Thread.currentThread().name) }
            .flatMapSingle { it ->
                CacheApiClient.apiClient.searchByQuery(
                    MainActivity.API_KEY,
                    "en",
                    it
                )
            }
            .doOnNext { Log.d("THR# After flatMap ", Thread.currentThread().name) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { Log.d("THR# after AnSchedulers", Thread.currentThread().name) }
            .subscribe({
                setMovies(it.results)
                Log.d(MainActivity.TAG, it.toString())
            }, {
                Log.e(MainActivity.TAG, it.toString())
            })


        // Получаем Single
        val getTopRatedMovies = MovieApiClient.apiClient.getTopRatedMovies(API_KEY, "ru")

        getTopRatedMovies.enqueue(object : Callback<MoviesResponse> {

            override fun onFailure(call: Call<MoviesResponse>, error: Throwable) {
                // Логируем ошибку
                Log.e(TAG, error.toString())
            }

            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {

                val movies = response.body()?.results
                // Передаем результат в adapter и отображаем элементы
                movies?.let {
                    movies_recycler_view.adapter = MoviesAdapter(movies, R.layout.list_item_movie)
                }
            }
        })

    }

    fun setMovies(movies: List<Movie>) {
        movies_recycler_view.adapter = MoviesAdapter(movies, R.layout.list_item_movie)
    }

    companion object {

        private val TAG = MainActivity::class.java.simpleName

        // TODO - insert your themoviedb.org API KEY here
        private val API_KEY = "Put Your Api Here"
    }
}
