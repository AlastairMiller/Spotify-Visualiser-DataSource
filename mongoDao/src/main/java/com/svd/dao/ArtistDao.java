package com.svd.dao;

import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedArtist;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Sorts.descending;

@Repository
public class ArtistDao extends AbstractDao<RefinedArtist> {

    public List<RefinedArtist> getAllByGenre(int offset, int limit, String genreName){
        BasicDBObject query = new BasicDBObject("genres", genreName);
        return mongoCollection.find().skip(offset).limit(limit).into(new ArrayList<>());
    }

    public List<RefinedArtist> getMostFollowedArtists(int offset, int limit){
        return mongoCollection.find().skip(offset).limit(limit).sort(descending("followers")).into(new ArrayList<>());
    }
}
