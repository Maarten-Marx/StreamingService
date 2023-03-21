package statements

import models.getUsers
import util.buildSql
import java.time.LocalDate
import kotlin.random.Random

suspend fun getUserStatement(count: Int): String {
    val users = getUsers(count)

    return buildSql {
        append("alter table user auto_increment = 1;")
        append("insert into user(name, profileImageUrl, creationDate) values")
        append(users.joinToString(",\n\t") {
            val date = LocalDate.now()
                .minusDays(100L + Random.nextLong(100L))
            "('${it.name.full}', '${it.picture.large}', '$date')"
        } + ";")
    }
}
