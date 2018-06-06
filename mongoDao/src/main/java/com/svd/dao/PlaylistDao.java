package com.svd.dao;

import com.mongodb.BasicDBObject;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedPlaylist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class PlaylistDao extends AbstractDao<RefinedPlaylist> {

    @Override
    public List<RefinedPlaylist> getMostPopular(int limit, int offset) {
        return mongoCollection.find().sort(new BsonDocument("numOfFollowers", new BsonString("-1"))).into(new ArrayList<>()).subList(offset, limit);
    }

    public List<RefinedPlaylist> getByUserId(String userId, int offset, int limit) {
        return mongoCollection.find().sort(new BsonDocument("userId", new BsonString(userId))).into(new ArrayList<>()).subList(offset, limit);
    }

    public List<RefinedPlaylist> getPlaylistsContainingTrack(String trackId, int offset, int limit) {
        BasicDBObject query = new BasicDBObject("trackIds", trackId);
        return mongoCollection.find(query).skip(offset).limit(limit).into(new ArrayList<>());
    }

}
