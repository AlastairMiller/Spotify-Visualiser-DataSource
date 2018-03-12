package com.svd.mapper;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import refinedDataModels.RefinedArtist;
import refinedDataModels.RefinedPlaylist;
import refinedDataModels.RefinedTrack;
import refinedDataModels.RefinedUser;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
public class JsontoRefinedMapper {


    public static RefinedArtist toArtist(Document document) {
        try {
            return new RefinedArtist(
                    document.getString("id"),
                    new URL(document.getString("url")),
                    new ArrayList<>(Arrays.asList(document.getString("genres").split(","))),
                    new ArrayList<>(Arrays.asList(document.getString("refinedArtistIds").split(","))),
                    document.getString("href"),
                    document.getString("name"),
                    document.getString("type"),
                    document.getInteger("followers"),
                    document.getInteger("popularity"),
                    new URI(document.getString("uri"))
            );
        } catch (NullPointerException e) {
            log.error("One or more fields are empty");
            return null;
        } catch (MalformedURLException | URISyntaxException e) {
            log.error("Malformed data from database");
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
                    document.getString("href"),
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
            log.error("Malformed data from database");
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
                    document.getString("href"),
                    new ArrayList<>(Arrays.asList(document.getString("imageUrls").split(",")))
            );
        } catch (NullPointerException e) {
            log.error("One or more fields are empty");
            return null;
        } catch (MalformedURLException | URISyntaxException e) {
            log.error("Malformed data from database");
            return null;
        }
    }

    public static RefinedPlaylist toPlaylist(Document document) {
        try {
            return new RefinedPlaylist(
                    document.getString("id"),
                    new URI(document.getString("uri")),
                    new URL(document.getString("externalURL")),
                    document.getInteger("numOfFollowers"),
                    document.getString("href"),
                    new ArrayList<>(Arrays.asList(document.getString("imageUrls").split(","))),
                    document.getString("name"),
                    document.getString("refinedUserId"),
                    new ArrayList<>(Arrays.asList(document.getString("refinedTracksIds").split(",")))
            );
        } catch (NullPointerException e) {
            log.error("One or more fields are empty");
            return null;
        } catch (MalformedURLException | URISyntaxException e) {
            log.error("Malformed data from database");
            return null;
        }
    }

}

