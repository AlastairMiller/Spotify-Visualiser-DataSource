package com.svd.dao;

import com.mongodb.client.model.Projections;
import com.svd.util.SortOrder;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedAlbum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

@Repository
public class AlbumDao extends AbstractDao<RefinedAlbum> {

    public List<String> getAlbumArtistIds(String albumId) {
        return getById(albumId).getArtistsIds();
    }

    public List<String> getAlbumTrackIds(String albumId) {
        return getById(albumId).getTrackIds();
    }

    public List<RefinedAlbum> getByReleaseDate(int offset, int limit) {
        return getByReleaseDate(offset, limit, SortOrder.Descending);
    }

    public List<RefinedAlbum> getByReleaseDate(int offset, int limit, SortOrder sortOrder) {
        if (sortOrder.equals(SortOrder.Descending)) {
            return mongoCollection.find().skip(offset).limit(limit).sort(orderBy(ascending("releaseDate"))).into(new ArrayList<>());
        }else {
            return mongoCollection.find().sort(orderBy(descending("releaseDate"))).into(new ArrayList<>());
        }
    }
}
