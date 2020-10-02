package ru.mikhailskiy.retrofitexample

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import ru.mikhailskiy.retrofitexample.network.MovieApiInterface


class RetroApp : Application() {
    var service: MovieApiInterface? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
    }


    fun isNetworkConnected(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }

    companion object {
        var instance: RetroApp? = null
            private set

        fun hasNetwork(): Boolean {
            return instance?.isNetworkConnected() ?: false
        }
    }
}