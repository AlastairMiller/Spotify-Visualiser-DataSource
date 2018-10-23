package al.miller.sv.ds.dao;

import al.miller.sv.ds.ClientHandler;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedArtist;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

@Repository
public class RefinedArtistDao extends AbstractDao<RefinedArtist> {


    public RefinedArtistDao(ClientHandler clientHandler, String collectionName) {
        super(clientHandler, collectionName);
    }

    public List<RefinedArtist> getAllByGenre(int offset, int limit, String genreName) {
        BasicDBObject query = new BasicDBObject("genres", genreName);
        return mongoCollection.find(query)
                .skip(offset)
                .limit(limit)
                .into(new ArrayList<>());
    }

    public List<RefinedArtist> getMostFollowedArtists(int offset, int limit) {
        return mongoCollection.find()
                .skip(offset)
                .limit(limit)
                .sort(orderBy(descending("followers")))
                .into(new ArrayList<>());
    }

    public List<Document> getMostPopularArtistsForCountry(String countryCode, int offset, int limit) {
        return clientHandler.getMongoDB().getCollection("artists_popular_in_" + countryCode).find()
                .skip(offset)
                .limit(limit)
                .sort(orderBy(descending("value")))
                .into(new ArrayList<>());
    }

    public List<Document> getMostPopularArtistsForCountry(String countryCode) {
        return clientHandler.getMongoDB().getCollection("artists_popular_in_" + countryCode).find()
                .sort(orderBy(descending("value")))
                .into(new ArrayList<>());
    }

    public List<Document> getMostPopularGenresForCountry(String countryCode) {
        return clientHandler.getMongoDB().getCollection("genres_popular_in_" + countryCode).find()

    }

}
