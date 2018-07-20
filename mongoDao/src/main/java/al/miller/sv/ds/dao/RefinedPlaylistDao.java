package al.miller.sv.ds.dao;

import al.miller.sv.ds.ClientHandler;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedPlaylist;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

@Repository
public class RefinedPlaylistDao extends AbstractDao<RefinedPlaylist> {


    public RefinedPlaylistDao(ClientHandler clientHandler, String collectionName) {
        super(clientHandler, collectionName);
    }

    @Override
    public List<RefinedPlaylist> getMostPopular(int offset, int limit) {
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

    public List<RefinedPlaylist> getPlaylistByCountry(String countryCode) {
        return mongoCollection.find(regex("name", ".*"+countryCode+"*."))
                .into(new ArrayList<>());
    }

}
