package al.miller.sv.ds.dao;

import al.miller.sv.ds.ClientHandler;
import com.mongodb.BasicDBObject;
import lombok.Data;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedPlaylist;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

@Repository
@Data
public class RefinedPlaylistDao extends AbstractDao<RefinedPlaylist> {

    private Map<String, LocalDateTime> mostPopularTracksTimeStamp = new HashMap<>();
    private Map<String, LocalDateTime> mostPopularArtistsTimeStamp = new HashMap<>();
    private Map<String, LocalDateTime> mostPopularGenresTimeStamp = new HashMap<>();

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

    public List<RefinedPlaylist> getPlaylistByCountry(String countryCode, int offset, int limit) {
        BasicDBObject query = new BasicDBObject("countryCode", countryCode);
        return mongoCollection.find(query)
                .skip(offset)
                .limit(limit)
                .into(new ArrayList<>());
    }


    public List<Document> getMostPopularTracks(String countryCode, int offset, int limit) {
        preformPopularTrackMapReduce(countryCode);
        return clientHandler.getMongoDB().getCollection("tracks_popular_in_" + countryCode)
                .find()
                .sort(orderBy(descending("value")))
                .skip(offset)
                .limit(limit)
                .into(new ArrayList<>());

    }


    public void preformPopularTrackMapReduce(String countryCode) {
        if (!mostPopularTracksTimeStamp.containsKey(countryCode) || mostPopularTracksTimeStamp.get(countryCode).isBefore(LocalDateTime.now().minusDays(1))) {
            String map = "function() { " +
                    "for(var i = 0; i < this.trackIds.length; i++) {" +
                    "var key = this.trackIds[i]; " +
                    "var value = 1; " +
                    "emit(key, value); " +
                    "}" +
                    "};";

            String reduce = "function(key, values) { return Array.sum(values) }";

            BasicDBObject query = new BasicDBObject("countryCode", countryCode);

            clientHandler.getMongoDB().getCollection("tracks_popular_in_" + countryCode).drop();
            clientHandler.getMongoDB().createCollection("tracks_popular_in_" + countryCode);

            mongoCollection.mapReduce(map, reduce, Document.class)
                    .filter(query)
                    .collectionName("tracks_popular_in_" + countryCode)
                    .into(new ArrayList<>());

            mostPopularTracksTimeStamp.put(countryCode, LocalDateTime.now());
        }
    }
}
