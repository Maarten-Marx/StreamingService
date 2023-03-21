package statements

import models.LoremType
import models.getLorem
import picsum
import util.buildSql
import java.time.LocalDate
import kotlin.random.Random
import kotlin.random.nextInt

suspend fun getPlaylistStatement(playlistCount: Int, userCount: Int): String {
    var words = getLorem(LoremType.Word, count = playlistCount * 5)
        .replace(".", "")
        .split(" ", ", ")
    val descriptions = getLorem(LoremType.Paragraph, count = playlistCount).split("\n\n")

    return buildSql {
        append("alter table playlist auto_increment = 1;")
        append("insert into playlist(name, description, coverUrl, public, ownerID, creationDate) values")
        append((0 until playlistCount).joinToString(",\n\t") { i ->
            val wordCount = Random.nextInt(1..5)
            val title = words.take(wordCount).joinToString(" ").lowercase().replaceFirstChar { it.titlecase() }
            words = words.drop(wordCount)

            val date = LocalDate.now()
                .minusDays(Random.nextLong(100L))

            "('$title', '${descriptions[i]}', '$picsum', ${Random.nextBoolean()}, ${Random.nextInt(1 until userCount)}, '${date}')"
        } + ";")
    }
}
