package com.svd.dao;

import com.svd.ClientHandler;
import refinedDataModels.RefinedPlaylist;

public class RefinedPlaylistDao extends BaseDao<RefinedPlaylist> {
    RefinedPlaylistDao(ClientHandler clientHandler) {
        super(clientHandler);
    }

    RefinedPlaylist retrieveEntryById(String itemId) {
        return (RefinedPlaylist) super.retrieveEntryById(itemId, "RefinedPlaylists");
    }
}
