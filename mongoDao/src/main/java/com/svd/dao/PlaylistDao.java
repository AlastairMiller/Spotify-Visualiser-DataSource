package com.svd.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.svd.ClientHandler;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedPlaylist;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

@Repository
public class PlaylistDao extends AbstractDao<RefinedPlaylist> {


    public PlaylistDao(ClientHandler clientHandler, String collectionName) {
        super(clientHandler, collectionName);
    }

    @Override
    public List<RefinedPlaylist> getMostPopular(int limit, int offset) {
        return mongoCollection.find()
                .skip(offset)
                .limit(limit)
                .sort(orderBy(descending("numOfFollowers")))
                .into(new ArrayList<>());
    }

    public List<RefinedPlaylist> getByUserId(String userId, int offset, int limit) {
        BasicDBObject query = new BasicDBObject("userId", userId);
        return mongoCollection.find(query)
                .skip(offset)
                .limit(limit)
                .sort(orderBy(descending("name")))
                .into(new ArrayList<>());
    }

    public List<RefinedPlaylist> getPlaylistsContainingTrack(String trackId, int offset, int limit) {
        BasicDBObject query = new BasicDBObject("trackIds", trackId);
        return mongoCollection.find(query)
                .skip(offset)
                .limit(limit)
                .into(new ArrayList<>());
    }

}
