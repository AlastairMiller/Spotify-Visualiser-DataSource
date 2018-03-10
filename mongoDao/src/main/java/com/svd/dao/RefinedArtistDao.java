package com.svd.dao;

import com.svd.ClientHandler;
import refinedDataModels.RefinedArtist;

public class RefinedArtistDao extends BaseDao<RefinedArtist> {
    RefinedArtistDao(ClientHandler clientHandler) {
        super(clientHandler);
    }
}

