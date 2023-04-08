/*
 * Objective: Get the title and length of any song that's at least 3 minutes long.
 */
select title, length
from song
where length >= 180;

/*
 * Objective: Get the names from all artists from the United States.
 */
select name
from artist
where countryCode = 'US';

/*
 * Get the name of each playlists owned by user 7.
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
select concat(name, ': ', description)
from playlist
where public;
