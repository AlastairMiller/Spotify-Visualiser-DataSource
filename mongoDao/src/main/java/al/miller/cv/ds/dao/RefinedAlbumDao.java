package al.miller.cv.ds.dao;

import al.miller.cv.ds.ClientHandler;
import al.miller.cv.ds.util.SortOrder;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedAlbum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.*;

@Repository
public class RefinedAlbumDao extends AbstractDao<RefinedAlbum> {


    public RefinedAlbumDao(ClientHandler clientHandler, String collectionName) {
        super(clientHandler, collectionName);
    }

    public List<String> getAlbumArtistIds(String albumId) {
        return getById(albumId)
                .getArtistsIds();
    }

    public List<String> getAlbumTrackIds(String albumId) {
        return getById(albumId)
                .getTrackIds();
    }

    public List<RefinedAlbum> geAllfromArtistId(int offset, int limit, String artistId) {
        return mongoCollection.find(eq("artistsIds", artistId))
                .skip(offset)
                .limit(limit)
                .into(new ArrayList<>());
    }

    public List<RefinedAlbum> getAllfromTrackId(int offset, int limit, String trackId) {
        return mongoCollection.find(eq("trackIds", trackId))
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
        return mongoCollection.find(eq("releaseDate", date.getTime()))
                .skip(offset)
                .limit(limit)
                .sort(orderBy(descending("name")))
                .into(new ArrayList<>());
    }
}
