package com.svd.dao;

import com.svd.ClientHandler;
import refinedDataModels.RefinedPlaylist;

public class RefinedUserDao extends BaseDao<RefinedPlaylist> {
    RefinedUserDao(ClientHandler clientHandler) {
        super(clientHandler);
    }
}
