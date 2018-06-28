package al.miller.cv.ds.dao;

import al.miller.cv.ds.ClientHandler;
import org.bson.BsonDocument;
import org.springframework.stereotype.Repository;

@Repository
public class PlaylistSimplifiedDao extends AbstractDao<BsonDocument> {

    public PlaylistSimplifiedDao(ClientHandler clientHandler, String collectionName) {
        super(clientHandler, collectionName);
    }

}
