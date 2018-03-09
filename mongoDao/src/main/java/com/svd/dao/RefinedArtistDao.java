package com.svd.dao;

import com.svd.ClientHandler;
import refinedDataModels.RefinedPlaylist;

public class RefinedArtistDao extends BaseDao<RefinedPlaylist> {
    RefinedArtistDao(ClientHandler clientHandler) {
        super(clientHandler);
    }
}

