/*
 * SIMPLE QUERIES
 */

/*
 * Objective: Get the title and length of any song that's at least 3 minutes long.
 */
select title, length
from song
where length >= 180;

/*
 * Objective: Get the names of all artists from the United States.
 */
select name
from artist
where countryCode = 'US';

/*
 * Get the name of each playlist owned by user 7.
 */
select name
from playlist
where ownerID = 7;

/*
 * Objective: Get the title of any instrumental song released in October 2022.
 */
select title
from song
where year(releaseDate) = 2022
  and month(releaseDate) = 10
  and lyrics is null;

/*
 * Get the name and description (in one field) of each public playlist.
 */
select concat(name, ': ', description) as 'playlist info'
from playlist
where public;

/*
 * JOINS
 */

/*
 * Get the titles of all songs on album 11.
 */

select title
from albumsong
    left join song on albumsong.songID = song.songID
where albumID = 11;

/*
 * Get the name of each playlist, along with the name of its owner.
 */

select playlist.name, user.name as 'owner'
from playlist
    left join user on user.userID = playlist.ownerID;

/*
 * Get the titles of all songs in playlist 5, each alongside the name of the playlist.
 */

select playlist.name as 'playlist name', song.title as 'song title'
from playlistsong
    left join playlist on playlistsong.playlistID = playlist.playlistID
    left join song on playlistsong.songID = song.songID
where playlistsong.playlistID = 5;

/*
 * Get the titles of all songs bookmarked by user 6 or 7
 */

select distinct song.title
from bookmark
    left join song on bookmark.songID = song.songID
where userID in (6, 7);

/*
 * Get the ID and title of each song released by at least one verified artist.
 */

select distinct song.songID, song.title
from songartist
    left join artist on songartist.artistID = artist.artistID
    left join song on songartist.songID = song.songID
where artist.verified;

/*
 * BASIC SUB-QUERIES
 */

/*
 * Get the names of all users that own a playlist.
 */

select name
from user
where userID in (select ownerID
                 from playlist);

/*
 * Get the titles of all songs in playlist 5, in order, without using joins.
 */

select title
from song
where songID in (select songID
                 from playlistsong
                 where playlistID = 5
                 order by songIndex);

/*
 * Get the names of all artists featured on album 19, without using joins.
 */

select name
from artist
where artistID in (select artistID
                   from songartist
                   where songID in (select songID
                                    from albumsong
                                    where albumID = 19));

/*
 * Get the names of all artists that user 1 follows, without using joins.
 */

select name
from artist
where artistID in (select artistID
                   from follower
                   where followerID = 1);

/*
 * Get the ID and title of any song not featured in any playlists.
 */

select songID, title
from song
where songID not in (select songID
                     from playlistsong);
