package com.svd.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.svd.ClientHandler;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import refinedDataModels.RefinedUser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.svd.mapper.RefinedtoJsonMapper.invokeSimpleGetter;

@Repository
@Slf4j
public class UserDao implements DaoInterface<RefinedUser> {

    protected ClientHandler clientHandler;
    protected MongoCollection<RefinedUser> mongoCollection;

    @Override
    public RefinedUser getById(String id) {
        BasicDBObject query = new BasicDBObject("id", id);
        return mongoCollection.find(query)
                .first();
    }

    @Override
    public List<RefinedUser> getMultipleById(List<String> ids) {
        BasicDBObject query = new BasicDBObject("id", ids);
        return mongoCollection.find(query)
                .into(new ArrayList<>());
    }

    @Override
    public RefinedUser getBySpotifyUri(String spotifyId) {
        BasicDBObject query = new BasicDBObject("spotifyURI", spotifyId);
        return mongoCollection.find(query)
                .first();
    }

    @Override
    public void save(RefinedUser inputObject) {
        mongoCollection.insertOne(inputObject);
    }

    @Override
    public void update(String id, RefinedUser newObject) {
        BasicDBObject query = new BasicDBObject("id", id);
        Document document = convertObjectToDocument(newObject);
        mongoCollection.findOneAndUpdate(query, document);
    }

    @Override
    public void delete(String id) {
        mongoCollection.findOneAndDelete(new BsonDocument("id", new BsonString(id)));
    }

    private Document convertObjectToDocument(RefinedUser inputObject) {
        Field[] fields = RefinedUser.class.getDeclaredFields();
        Document document = new Document();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Method getter;
                if (field.getType().getName().equals("boolean")) {
                    getter = RefinedUser.class.getDeclaredMethod("is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                } else {
                    getter = RefinedUser.class.getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                }
                document.append(field.getName(), invokeSimpleGetter(getter, inputObject));
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage());
            }
        }
        return document;
    }
}
