package statements

import models.LoremType
import models.getLorem
import picsum
import songCount
import util.buildSql
import java.time.LocalDate
import kotlin.random.Random
import kotlin.random.nextInt

suspend fun getAlbumStatement(albumCount: Int): String {
    var words = getLorem(LoremType.Word, count = albumCount * 5)
        .replace(".", "")
        .split(" ", ", ")

    return buildSql {
        append("alter table album auto_increment = 1;")
        append("insert into album(title, releaseDate, coverUrl) values")
        append((0 until songCount).joinToString(",\n\t") {
            val wordCount = Random.nextInt(1..5)
            val title = words.take(wordCount).joinToString(" ").lowercase().replaceFirstChar { it.titlecase() }
            words = words.drop(wordCount)

            val date = LocalDate.now()
                .minusDays(Random.nextLong(100L))

            "('$title', '$date', '$picsum')"
        } + ";")
    }
}