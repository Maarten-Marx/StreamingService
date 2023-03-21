drop schema if exists streaming_service;

create schema streaming_service;
use streaming_service;

create table Album
(
    albumID int auto_increment primary key,
    title varchar(500) not null,
    releaseDate date not null,
    coverUrl varchar(200) not null
);

create table Artist
(
    artistID int auto_increment primary key,
    name varchar(100) not null unique,
    description varchar(1000),
    verified boolean not null,
    profileImageUrl varchar(200),
    countryCode varchar(2)
);

create table Song
(
    songID int auto_increment primary key,
    title varchar(500) not null,
    length int not null,
    releaseDate date not null,
    lyrics varchar(5000)
);

create table User
(
    userID int auto_increment primary key,
    name varchar(100) not null unique,
    profileImageUrl varchar(200),
    creationDate date not null
);

create table Playlist
(
    playlistID int auto_increment primary key,
    name varchar(200) not null,
    description varchar(1000) not null,
    coverUrl varchar(200),
    public boolean not null,
    ownerID int not null,
    creationDate date not null,
    constraint playlist_user_FK foreign key (ownerID) references User (userID)
);

create table AlbumSong
(
    albumID int not null,
    songID int not null,
    songIndex int not null,
    constraint albumSong_album_FK foreign key (albumID) references Album (albumID),
    constraint albumSong_song_FK foreign key (songID) references Song (songID)
);

create table SongArtist
(
    songID int not null,
    artistID int not null,
    constraint songArtist_song_FK foreign key (songID) references Song (songID),
    constraint songArtist_artist_FK foreign key (artistID) references Artist (artistID)
);

create table Follower
(
    artistID int not null,
    followerID int not null,
    constraint follower_artist_FK foreign key (artistID) references Artist (artistID),
    constraint follower_user_FK foreign key (followerID) references User (userID)
);

create table Bookmark
(
    songID int not null,
    userID int not null,
    constraint bookmark_song_FK foreign key (songID) references Song (songID),
    constraint bookmark_user_FK foreign key (userID) references User (userID)
);

create table PlaylistSong
(
    playlistID int not null,
    songID int not null,
    songIndex int not null,
    constraint playlistSong_playlist_FK foreign key (playlistID) references Playlist (playlistID),
    constraint playlistSong_song_FK foreign key (songID) references Song (songID)
)
