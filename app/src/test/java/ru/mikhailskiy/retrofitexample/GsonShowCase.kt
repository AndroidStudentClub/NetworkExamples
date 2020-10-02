package ru.mikhailskiy.retrofitexample

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.junit.Assert
import org.junit.Test
import ru.mikhailskiy.retrofitexample.convertors.Custom
import ru.mikhailskiy.retrofitexample.convertors.CustomConverter

class GsonExample {


    @Test
    fun toJsonExample() {
        val expectedString =
            "{\"title\":\"Android School Intensiv\",\"year\":2020,\"imageUrl\":\"\",\"genre\":\"education\",\"country\":\"Russia\"}"
        val sample = Gson().toJson(SimpleMovie.createSample())
        Assert.assertEquals(expectedString, sample)
        println(sample)
    }

    @Test
    fun fromJsonExample() {
        val gson = Gson()
        val sample = gson.toJson(SimpleMovie.createSample())
        val fromJsonMovie: SimpleMovie = gson.fromJson(sample, SimpleMovie::class.java)

        Assert.assertEquals(SimpleMovie.createSample(), fromJsonMovie)
        println(fromJsonMovie)
    }

    @Test
    fun fromJsonWithSerializedNameExample() {
        val gson = Gson()
        val sample = gson.toJson(SimpleMovie.createSample())
        println(sample)
    }

    @Test
    fun withoutSerializeNullsExample() {
        val expected =
            "{\"title\":\"Spider-man\",\"year\":1998,\"genre\":\"Action\",\"country\":\"USA\"}"
        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create()
        val movie = SimpleMovie("Spider-man", 1998, null, "Action", "USA")
        val movieJson = gson.toJson(movie)

        Assert.assertEquals(expected, movieJson)
        println(movieJson)
    }

    @Test
    fun serializeNullsExample() {
        val expected =
            "{\"title\":\"Spider-man\",\"year\":2000,\"imageUrl\":null,\"genre\":\"Action\",\"country\":\"USA\"}"
        val gsonBuilder = GsonBuilder()
        // Обратите внимание
        gsonBuilder.serializeNulls()
        val gson = gsonBuilder.create()
        val movie = SimpleMovie("Spider-man", 2000, null, "Action", "USA")
        val movieJson = gson.toJson(movie)

        Assert.assertEquals(expected, movieJson)
        println(movieJson)
    }

    @Test
    fun setFieldNamingExample() {
        val expected =
            "{\"title\":\"Spider-man\",\"year\":1999,\"image-url\":\"testurl\",\"genre\":\"Action\",\"country\":\"USA\"}"
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
        val gson = gsonBuilder.create()
        val movie = SimpleMovie("Spider-man", 1999, "testurl", "Action", "USA")
        val movieJson = gson.toJson(movie)

        Assert.assertEquals(expected, movieJson)
        println(movieJson)
    }

    @Test
    fun setVersionNamingExample() {
        val expected = "{\"id\":1,\"name\":\"John Snow\"}"
        val builder = GsonBuilder()
        builder.setVersion(1.0)
        val gson = builder.create()
        val student = Student()
        student.id = 1
        student.name = "John Snow"
        student.isVerified = true
        val jsonString = gson.toJson(student)

        Assert.assertEquals(expected, jsonString)
        println(jsonString)
    }

    @Test
    fun setVersionNamingExample2() {
        val expected = "{\"id\":1,\"name\":\"John Snow\",\"isVerified\":true}"
        val student = Student()
        student.id = 1
        student.name = "John Snow"
        student.isVerified = true
        val gson = Gson()
        val jsonString = gson.toJson(student)

        Assert.assertEquals(expected, jsonString)
        println(jsonString)
    }

    @Test
    fun prettyPrintExample() {
        val gsonBuilder = GsonBuilder()
        // Обратите внимание
        gsonBuilder.setPrettyPrinting()
        val gson = gsonBuilder.create()
        val movie = SimpleMovie("Spider-man", 1999, "", "Action", "USA")
        val movieJson = gson.toJson(movie)
        println(movieJson)
    }

    @Test
    fun customConverterDemo() {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Custom::class.java, CustomConverter())
        val gson = builder.create()
        val custom = Custom("5.02.2020", 123)
        val sample = gson.toJson(custom)
        println(sample)
    }
}