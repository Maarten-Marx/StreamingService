package statements

import util.buildSql
import kotlin.random.Random
import kotlin.random.nextInt

fun getLinkStatements(albumCount: Int, artistCount: Int, playlistCount: Int, songCount: Int, userCount: Int) = buildSql {
    append(getAlbumSongLinks(albumCount, songCount))
    append(getBookmarkLinks(songCount, userCount))
    append(getFollowerLinks(artistCount, userCount))
    append(getPlaylistSongLink(playlistCount, songCount))
    append(getSongArtistLink(songCount, artistCount))
}

fun getAlbumSongLinks(albumCount: Int, songCount: Int) = buildSql {
    val albumSongPairs = mutableSetOf<Pair<Int, Int>>()

    (1..albumCount).forEach { albumID ->
        albumSongPairs.add(albumID to Random.nextInt(1..songCount))
    }
    (1..songCount).filter { !albumSongPairs.any { p -> p.second == it } }.forEach { songID ->
        albumSongPairs.add(
            Random.nextInt(1..albumCount) to songID
        )
    }

    append("insert into albumsong(albumID, songID, songIndex) values")
    append(albumSongPairs.groupBy { it.first }.toList().joinToString(",\n\t") {
        it.second.mapIndexed { index, pair ->
            "(${pair.first}, ${pair.second}, $index)"
        }.joinToString(",\n\t")
    } + ";")
}

fun getBookmarkLinks(songCount: Int, userCount: Int) = buildSql {
    val bookmarks = mutableMapOf<Int, List<Int>>()
    
    (1..userCount).forEach { userID ->
        (1..songCount)
            .shuffled()
            .take(Random.nextInt(0..5))
            .ifEmpty { null }?.let {
                bookmarks[userID] = it
            }
    }

    append("insert into bookmark(songID, userID) values")
    append(bookmarks.entries.joinToString(",\n\t") { pair ->
        pair.value.joinToString(",\n\t") {
            "($it, ${pair.key})"
        }
    } + ";")
}

fun getFollowerLinks(artistCount: Int, userCount: Int) = buildSql {
    val followers = mutableMapOf<Int, List<Int>>()

    (1..artistCount).forEach { artistID ->
        (1..userCount)
            .shuffled()
            .take(Random.nextInt(0..10))
            .ifEmpty { null }?.let {
                followers[artistID] = it
            }
    }

    append("insert into follower(artistID, followerID) values")
    append(followers.entries.joinToString(",\n\t") { pair ->
        pair.value.joinToString(",\n\t") {
            "(${pair.key}, $it)"
        }
    } + ";")
}

fun getPlaylistSongLink(playlistCount: Int, songCount: Int) = buildSql {
    val playlistSongLinks = mutableMapOf<Int, List<Int>>()

    (1..playlistCount).forEach { playlistID ->
         playlistSongLinks[playlistID] = (1..songCount)
            .shuffled()
            .take(Random.nextInt(1..10))
    }

    append("insert into playlistsong(playlistID, songID, songIndex) values")
    append(playlistSongLinks.entries.joinToString(",\n\t") { pair ->
        pair.value.mapIndexed { index, songID ->
            "(${pair.key}, $songID, $index)"
        }.joinToString(",\n\t")
    } + ";")
}

fun getSongArtistLink(songCount: Int, artistCount: Int) = buildSql {
    val songArtistLinks = mutableMapOf<Int, List<Int>>()

    (1..songCount).forEach { songID ->
        songArtistLinks[songID] = (1..artistCount)
            .shuffled()
            .take(Random.nextInt(1..3))
    }

    append("insert into songartist(songID, artistID) values")
    append(songArtistLinks.entries.joinToString(",\n\t") { pair ->
        pair.value.joinToString(",\n\t") {
            "(${pair.key}, $it)"
        }
    } + ";")
}
