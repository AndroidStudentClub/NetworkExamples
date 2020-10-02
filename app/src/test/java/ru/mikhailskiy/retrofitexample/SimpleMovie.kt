package ru.mikhailskiy.retrofitexample

import com.google.gson.annotations.SerializedName

data class SimpleMovie(
    var title: String?,
    var year: Int,
    var imageUrl: String?,
    var genre: String?,
    @SerializedName(value = "country", alternate = ["Country"])
    var movieCountry: String
) {

    companion object {
        fun createSample(): SimpleMovie {
            return SimpleMovie("Android School Intensiv", 2020, "", "education", "Russia")
        }
    }
}