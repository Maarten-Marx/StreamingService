package statements

import kotlinx.coroutines.runBlocking
import models.LoremType
import models.getLorem
import models.getUsers
import util.buildSql
import kotlin.random.Random

suspend fun getArtistStatement(count: Int): String {
    val users = getUsers(count)

    val descriptions = runBlocking { getLorem(LoremType.Paragraph, count = count) }.split("\n\n")
    descriptions.forEachIndexed { index, d ->
        users[index].description = d
    }

    return buildSql {
        append("alter table artist auto_increment = 1;")
        append("insert into artist(name, description, verified, profileImageUrl, countryCode) values")
        append(users.joinToString(",\n\t") {
            "('${it.login.username}', '${it.description}', ${Random.nextBoolean()}, '${it.picture.large}', '${it.nat}')"
        } + ";")
    }
}