package com.svd.dao;

import com.svd.ClientHandler;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import org.springframework.stereotype.Repository;

@Repository
public class PlaylistSimplifiedDao extends AbstractDao<PlaylistSimplified> {

    public PlaylistSimplifiedDao(ClientHandler clientHandler, String collectionName) {
        super(clientHandler, collectionName);
    }


}
