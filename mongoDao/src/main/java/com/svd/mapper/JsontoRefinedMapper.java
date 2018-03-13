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
            return new RefinedArtist(
                    document.getString("id"),
                    new URL(document.getString("externalURL")),
                    new ArrayList<>((Collection<? extends String>) document.get("genres")),
                    new ArrayList<>((Collection<? extends String>) document.get("refinedAlbumIds")),
                    new URL(document.getString("href")),
                    document.getString("name"),
                    document.getInteger("followers"),
                    document.getInteger("popularity"),
                    new URI(document.getString("uri"))
            );
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

            return new RefinedTrack(
                    document.getString("id"),
                    new ArrayList<>((Collection<? extends String>) document.get("refinedAlbumIds")),
                    new ArrayList<>((Collection<? extends String>) document.get("refinedArtistIds")),
                    document.getInteger("discNum"),
                    document.getInteger("durationMs"),
                    document.getBoolean("explicit"),
                    new URL(document.getString("externalURL")),
                    new URL(document.getString("href")),
                    document.getString("name"),
                    new URL(document.getString("previewURL")),
                    document.getInteger("trackNumber"),
                    document.getInteger("popularity"),
                    new URI(document.getString("uri"))
            );
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
            return new RefinedUser(
                    document.getString("id"),
                    new URI(document.getString("uri")),
                    document.getString("displayName"),
                    new URL(document.getString("externalURL")),
                    document.getInteger("numOfFollowers"),
                    new URL(document.getString("href")),
                    new ArrayList<>(Arrays.asList(document.getString("imageUrls").split(",")))
            );
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

