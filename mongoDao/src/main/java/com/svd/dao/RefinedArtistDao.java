package com.svd.dao;

import com.mongodb.BasicDBObject;
import com.svd.ClientHandler;
import refinedDataModels.RefinedArtist;

public class RefinedArtistDao extends BaseDao<RefinedArtist> {
    RefinedArtistDao(ClientHandler clientHandler) {
        super(clientHandler);
    }
    RefinedArtist retrieveEntryById(String itemId) {
        return (RefinedArtist) super.retrieveEntryById(itemId, "RefinedArtists");
    }
}

