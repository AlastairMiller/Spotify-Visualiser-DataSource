package com.svd.dao;

import com.mongodb.BasicDBObject;
import com.svd.util.SortOrder;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedAlbum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Sorts.*;

@Repository
public class AlbumDao extends AbstractDao<RefinedAlbum> {

    public List<String> getAlbumArtistIds(String albumId) {
        return getById(albumId)
                .getArtistsIds();
    }

    public List<String> getAlbumTrackIds(String albumId) {
        return getById(albumId)
                .getTrackIds();
    }

    public List<RefinedAlbum> geAllfromArtistId(int offset, int limit, String artistId) {
        BasicDBObject query = new BasicDBObject("artistIds", artistId);
        return mongoCollection.find(query)
                .skip(offset)
                .limit(limit)
                .into(new ArrayList<>());
    }

    public List<RefinedAlbum> getAllfromTrackId(int offset, int limit, String trackId) {
        BasicDBObject query = new BasicDBObject("trackIds", trackId);
        return mongoCollection.find(query)
                .skip(offset)
                .limit(limit)
                .into(new ArrayList<>());
    }

    public List<RefinedAlbum> getByReleaseDate(int offset, int limit) {
        return getByReleaseDate(offset, limit, SortOrder.Descending);
    }

    public List<RefinedAlbum> getByReleaseDate(int offset, int limit, SortOrder sortOrder) {
        if (sortOrder.equals(SortOrder.Descending)) {
            return mongoCollection.find()
                    .skip(offset)
                    .limit(limit)
                    .sort(orderBy(ascending("releaseDate")))
                    .into(new ArrayList<>());
        } else {
            return mongoCollection.find()
                    .skip(offset)
                    .limit(limit)
                    .sort(orderBy(descending("releaseDate")))
                    .into(new ArrayList<>());
        }
    }

    public List<RefinedAlbum> getAlbumsFromDate(int offset, int limit, Date date) {
        BasicDBObject query = new BasicDBObject("releaseDate", date);
        return mongoCollection.find(query)
                .skip(offset)
                .limit(limit)
                .sort(orderBy(descending("name")))
                .into(new ArrayList<>());
    }
}
