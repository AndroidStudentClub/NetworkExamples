package ru.mikhailskiy.retrofitexample

import com.google.gson.annotations.Since

internal class Student {
    @Since(1.0)
    var id = 0

    @Since(1.0)
    var name: String? = null

    @Since(1.1)
    var isVerified = false
}