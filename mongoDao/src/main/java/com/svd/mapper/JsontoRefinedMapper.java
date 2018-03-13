package com.svd.mapper;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import refinedDataModels.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
public class JsontoRefinedMapper {

    public static RefinedAlbum toAlbum(Document document) {
        try {
            return RefinedAlbum.builder()
                    .id(document.getString("id"))
                    .name(document.getString("name"))
                    .artistsIds(new ArrayList<>((Collection<? extends String>) document.get("artistsIds")))
                    .trackIds(new ArrayList<>((Collection<? extends String>) document.get("trackIds")))
                    .imageURL(new URL(document.getString("imageURL")))
                    .releaseDate(document.getDate("releaseDate"))
                    .popularity(document.getInteger("popularity"))
                    .externalURL(new URL(document.getString("externalURL")))
                    .href(new URL(document.getString("href")))
                    .uri(new URI(document.getString("uri")))
                    .build();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RefinedArtist toArtist(Document document) {
        try {
            return RefinedArtist.builder()
                    .id(document.getString("id"))
                    .externalURL(new URL(document.getString("externalURL")))
                    .genres( new ArrayList<>((Collection<? extends String>) document.get("genres")))
                    .refinedAlbumIds(new ArrayList<>((Collection<? extends String>) document.get("refinedAlbumIds")))
                    .href(new URL(document.getString("href")))
                    .name( document.getString("name"))
                    .followers(document.getInteger("followers"))
                    .popularity( document.getInteger("popularity"))
                    .uri( new URI(document.getString("uri")))
                    .build();
        } catch (NullPointerException e) {
            log.error("One or more fields are empty");
            return null;
        } catch (MalformedURLException | URISyntaxException e) {
            log.error("Malformed Artist URL/URI from database");
            return null;
        }
    }

    public static RefinedTrack toTrack(Document document) {
        try {
            return RefinedTrack.builder()
                    .id(document.getString("id"))
                    .refinedAlbumIds(new ArrayList<>((Collection<? extends String>) document.get("refinedAlbumIds")))
                    .refinedArtistIds(new ArrayList<>((Collection<? extends String>) document.get("refinedArtistIds")))
                    .discNum(document.getInteger("discNum"))
                    .durationMs(document.getInteger("durationMs"))
                    .explicit(document.getBoolean("explicit"))
                    .externalURL(new URL(document.getString("externalURL")))
                    .href(new URL(document.getString("href")))
                    .name(document.getString("name"))
                    .previewURL(new URL(document.getString("previewURL")))
                    .trackNumber(document.getInteger("trackNumber"))
                    .popularity(document.getInteger("popularity"))
                    .uri(new URI(document.getString("uri")))
                    .build();
        } catch (NullPointerException e) {
            log.error("One or more fields are empty");
            return null;
        } catch (MalformedURLException | URISyntaxException e) {
            log.error("Malformed Track URL/URI from database");
            return null;
        }
    }

    public static RefinedUser toUser(Document document) {
        try {
            return RefinedUser.builder()
                    .id(document.getString("id"))
                    .displayName(document.getString("displayName"))
                    .externalURL(new URL(document.getString("externalURL")))
                    .numOfFollowers(document.getInteger("numOfFollowers"))
                    .href(new URL(document.getString("href")))
                    .imageURL(new URL(document.getString("imageURL")))
                    .uri(new URI(document.getString("uri")))
                    .build();
        } catch (NullPointerException e) {
            log.error("One or more fields are empty");
            return null;
        } catch (MalformedURLException | URISyntaxException e) {
            log.error("Malformed User URL/URI from database");
            return null;
        }
    }

    public static RefinedPlaylist toPlaylist(Document document) {
        try {
            return RefinedPlaylist.builder()
                    .id(document.getString("id"))
                    .externalURL(new URL(document.getString("externalURL")))
                    .numOfFollowers(document.getInteger("numOfFollowers"))
                    .href(new URL(document.getString("href")))
                    .imageURL(new URL(document.getString("imageURL")))
                    .name(document.getString("name"))
                    .description(document.getString("description"))
                    .refinedUserId(document.getString("refinedUserId"))
                    .refinedTrackIds(new ArrayList<String>((Collection<? extends String>) document.get("refinedTrackIds")))
                    .uri(new URI(document.getString("uri")))
                    .build();
        } catch (NullPointerException e) {
            log.error("One or more fields are empty");
            return null;
        } catch (MalformedURLException | URISyntaxException e) {
            log.error("Malformed Playlist URL/URI from database");
            return null;
        }
    }

}

