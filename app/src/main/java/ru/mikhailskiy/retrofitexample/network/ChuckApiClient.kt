package ru.mikhailskiy.retrofitexample.network

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mikhailskiy.retrofitexample.network.logger.CustomHttpLogging

object ChuckApiClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    fun apiClient(context: Context): MovieApiInterface {

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(ChuckInterceptor(context))
            .addInterceptor(HttpLoggingInterceptor(CustomHttpLogging()).apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            })
            .authenticator(DemoAuthenticator())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(MovieApiInterface::class.java)
    }
}