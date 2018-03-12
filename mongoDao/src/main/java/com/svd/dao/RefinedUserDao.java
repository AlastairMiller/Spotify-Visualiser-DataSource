package com.svd.dao;

import com.svd.ClientHandler;
import refinedDataModels.RefinedUser;

public class RefinedUserDao extends BaseDao<RefinedUser> {
    RefinedUserDao(ClientHandler clientHandler) {
        super(clientHandler);
    }

    RefinedUser retrieveEntryById(String itemId) {
        return (RefinedUser) super.retrieveEntryById(itemId, "RefinedUser");
    }
}
