
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.jetbrains.exposed.sql.Database
import statements.*
import util.SqlBuilder
import java.io.File
import kotlinx.serialization.json.Json

/** Docs @ https://randomuser.me/documentation */
const val userApi = "https://randomuser.me/api"
/** Docs @ https://asdfast.beobit.net/docs/ */
const val loremApi = "https://asdfast.beobit.net/api/"
const val picsum = "https://picsum.photos/512"

val client = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

const val albumCount = 25
const val artistCount = 15
const val playlistCount = 15
const val songCount = 40
const val userCount = 20

suspend fun main() {
    val dbUser = System.getenv("db-user")
    val dbPassword = System.getenv("db-password")

    Database.connect("jdbc:mysql://localhost:3306/streaming_service", user = dbUser, password = dbPassword)

    val builder = SqlBuilder()

    for (table in listOf(
        "albumsong", "bookmark", "follower", "playlistsong", "songartist",
        "album", "artist", "playlist", "song", "user"
    )) {
        builder.append("delete from $table where true;")
    }

    builder.append(getAlbumStatement(albumCount))
    builder.append(getArtistStatement(artistCount))
    builder.append(getSongStatement(songCount))
    builder.append(getUserStatement(userCount))
    builder.append(getPlaylistStatement(playlistCount, userCount))
    builder.append(getLinkStatements(albumCount, artistCount, playlistCount, songCount, userCount))

    File("./result.sql").writeText(builder.result())
}
