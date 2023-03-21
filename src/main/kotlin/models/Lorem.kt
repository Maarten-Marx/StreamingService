package models

import client
import io.ktor.client.call.*
import io.ktor.client.request.*
import loremApi
import kotlin.random.Random
import kotlin.random.nextInt
import kotlinx.serialization.Serializable

suspend fun getLorem(type: LoremType, countRange: IntRange? = null, count: Int? = null): String {
    val res: LoremResponse = client.get(loremApi) {
        parameter("type", type.paramName)
        parameter("length", countRange?.let { Random.nextInt(it) } ?: count)
    }.body()

    return res.text
}

@Serializable
data class LoremResponse(val text: String)

enum class LoremType(val paramName: String) {
    Word("word"), Paragraph("paragraph")
}
