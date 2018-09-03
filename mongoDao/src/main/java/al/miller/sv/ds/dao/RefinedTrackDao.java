package al.miller.sv.ds.dao;

import al.miller.sv.ds.ClientHandler;
import com.mongodb.BasicDBObject;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedTrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Sorts.*;

@Repository
public class RefinedTrackDao extends AbstractDao<RefinedTrack> {


    public RefinedTrackDao(ClientHandler clientHandler, String collectionName) {
        super(clientHandler, collectionName);
    }

    public List<RefinedTrack> getAllOnAlbum(String albumId) {
        BasicDBObject query = new BasicDBObject("albumId", albumId);
        return mongoCollection.find(query)
                .sort(orderBy(ascending("trackNumber")))
                .into(new ArrayList<>());
    }

    public List<RefinedTrack> getAllFromArtist(String artistId) {
        BasicDBObject query = new BasicDBObject("artistIds", artistId);
        return mongoCollection.find(query)
                .sort(orderBy(descending("albumId")))
                .into(new ArrayList<>());
    }


}