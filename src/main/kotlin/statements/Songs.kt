package statements

import models.LoremType
import models.getLorem
import util.buildSql
import java.time.LocalDate
import kotlin.random.Random
import kotlin.random.nextInt

suspend fun getSongStatement(songCount: Int): String {
    var words = getLorem(LoremType.Word, count = songCount * 5)
        .replace(".", "")
        .split(" ", ", ")
    var paragraphs = getLorem(LoremType.Paragraph, count = songCount * 2).split("\n\n")

    return buildSql {
        append("alter table song auto_increment = 1;")
        append("insert into song(title, length, releaseDate, lyrics) values")
        append((0 until songCount).joinToString(",\n\t") {
            val wordCount = Random.nextInt(1..5)
            val title = words.take(wordCount).joinToString(" ").lowercase().replaceFirstChar { it.titlecase() }
            words = words.drop(wordCount)

            val paragraphCount = Random.nextInt(0..2)
            val lyrics = if (paragraphCount > 0) {
                paragraphs = paragraphs.drop(paragraphCount)
                paragraphs.take(paragraphCount).joinToString("")
            } else {
                null
            }

            val date = LocalDate.now()
                .minusDays(100 + Random.nextLong(100L))

            "('$title', ${Random.nextInt(90..300)}, '$date', ${if (lyrics != null) "'$lyrics'" else "null"})"
        } + ";")
    }
}