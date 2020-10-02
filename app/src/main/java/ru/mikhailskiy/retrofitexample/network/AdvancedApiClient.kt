package ru.mikhailskiy.retrofitexample.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mikhailskiy.retrofitexample.network.logger.CustomHttpLogging

object AdvancedApiClient : BaseApi() {
    @JvmStatic
    val client: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor(CustomHttpLogging())
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("X-Auth-Token", "sampletoken")
                        .build()
                    chain.proceed(request)
                })
                .addInterceptor(interceptor)
                .build()
            return Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
}