package ru.mikhailskiy.retrofitexample.convertors

import com.google.gson.*
import java.lang.reflect.Type

class CustomConverter : JsonSerializer<Custom>, JsonDeserializer<Custom> {

    override fun serialize(
        src: Custom, type: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val jsonObject = JsonObject()
        // Здесь можно описать кастомную логику
        jsonObject.addProperty("date", src.date)
        jsonObject.addProperty("integer", src.integer.toString())
        return jsonObject
    }

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement, type: Type,
        context: JsonDeserializationContext
    ): Custom {
        val jsonObject = json.asJsonObject
        // Здесь можно описать кастомную логику
        val date = jsonObject["date"].asString
        val integer: Int = (jsonObject["integer"].asString).toInt()
        return Custom(date, integer)
    }
}