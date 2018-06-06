package com.svd.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.svd.ClientHandler;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.svd.mapper.RefinedtoJsonMapper.invokeSimpleGetter;

@Slf4j
public abstract class AbstractDao<T> {

    @SuppressWarnings("unchecked")
    protected Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    protected ClientHandler clientHandler;
    protected MongoCollection<T> mongoCollection;

    public T getById(String id) {
        BasicDBObject query = new BasicDBObject("id", id);
        return mongoCollection.find(query).first();
    }

    public List<T> getMultipleById(List<String> ids) {
        BasicDBObject query = new BasicDBObject("id", ids);
        return mongoCollection.find(query).into(new ArrayList<>());
    }

    public T getBySpotifyUri(String spotifyId) {
        BasicDBObject query = new BasicDBObject("spotifyURI", spotifyId);
        return mongoCollection.find(query).first();
    }

    public List<T> getAllMatchingNames(String name) {
        BasicDBObject query = new BasicDBObject("name", name);
        return mongoCollection.find(query).into(new ArrayList<>());
    }

    public List<T> getMostPopular(int limit, int offset) {
        return mongoCollection.find().sort(new BsonDocument("popularity", new BsonString("-1"))).into(new ArrayList<>());
    }

    public void save(T inputObject) {
        mongoCollection.insertOne(inputObject);
    }

    public void update(String id, T newObject) {
        BasicDBObject query = new BasicDBObject("id", id);
        Document document = convertObjectToDocument(newObject);
        mongoCollection.findOneAndUpdate(query, document);
    }

    public void delete(String id) {
        mongoCollection.findOneAndDelete(new BsonDocument("id", new BsonString(id)));
    }

    private Document convertObjectToDocument(T inputObject) {
        Field[] fields = clazz.getDeclaredFields();
        Document document = new Document();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Method getter;
                if (field.getType().getName().equals("boolean")) {
                    getter = clazz.getDeclaredMethod("is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                } else {
                    getter = clazz.getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                }
                document.append(field.getName(), invokeSimpleGetter(getter, inputObject));
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage());
            }
        }
        return document;
    }
}
