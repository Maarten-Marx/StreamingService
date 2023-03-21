package models

import client
import io.ktor.client.call.*
import io.ktor.client.request.*
import userApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

suspend fun getUsers(count: Int): List<UserData> {
    val res: UserResponse = client.get(userApi) {
        parameter("results", count)
    }.body()

    return res.results
}

@Serializable
data class UserResponse(val results: List<UserData>)

@Serializable
data class UserData(
    val name: Name,
    val login: Login,
    val picture: Picture,
    val nat: String
) {
    @Transient
    var description: String? = null

    @Serializable
    data class Name(val first: String, val last: String) {
        @Transient
        val full: String = "$first $last"
    }

    @Serializable
    data class Login(val username: String)

    @Serializable
    data class Picture(val large: String)
}
