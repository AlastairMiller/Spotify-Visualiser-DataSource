package com.svd.mapper;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import refinedDataModels.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JsontoRefinedMapper {

    public static RefinedAlbum toAlbum(Document document) {
        return (RefinedAlbum) Builder.start(RefinedAlbum.class).with(documentToMap(document)).build();
    }

    public static RefinedArtist toArtist(Document document) {
        return (RefinedArtist) Builder.start(RefinedArtist.class).with(documentToMap(document)).build();
    }

    public static RefinedTrack toTrack(Document document) {
        return (RefinedTrack) Builder.start(RefinedTrack.class).with(documentToMap(document)).build();
    }

    public static RefinedUser toUser(Document document) {
        return (RefinedUser) Builder.start(RefinedUser.class).with(documentToMap(document)).build();
    }


    public static RefinedPlaylist toPlaylist(Document document) {
        return (RefinedPlaylist) Builder.start(RefinedPlaylist.class).with(documentToMap(document)).build();
    }

    private static Map<String, Object> documentToMap(Document document) {
        Map<String, Object> mapTo = new HashMap<>();
        for (String key : document.keySet()) {
            if (document.get(key) != null && !key.equals("_id")) {
                mapTo.put(key, document.get(key));
            }
        }
        return mapTo;
    }


}

