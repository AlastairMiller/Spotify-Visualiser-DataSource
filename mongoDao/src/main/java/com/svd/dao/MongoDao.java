package com.svd.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.svd.ClientHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.bitsAllClear;
import static com.mongodb.client.model.Filters.eq;
import static com.svd.mapper.RefinedtoJsonMapper.invokeSimpleGetter;

@Slf4j
@Data
@Deprecated
public class MongoDao<T> {

    private final ClientHandler clientHandler;
    private final String collectionName;
    private final Class<T> daoClazz;
    private MongoCollection<Document> mongoCollection;

    public MongoDao(ClientHandler clientHandler, String collectionName, Class<T> clazz) {
        this.clientHandler = clientHandler;
        this.collectionName = collectionName;
        this.mongoCollection = clientHandler.getMongoDB().getCollection(collectionName);
        this.daoClazz = clazz;
    }

    public void saveEntryToDatabase(T inputObject) {
        Field[] fields = daoClazz.getDeclaredFields();
        Document doc = new Document();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Method getter;
                if (field.getType().getName().equals("boolean")) {
                    getter = daoClazz.getDeclaredMethod("is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                } else {
                    getter = daoClazz.getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                }
                doc.append(field.getName(), invokeSimpleGetter(getter, inputObject));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        mongoCollection.insertOne(doc);
    }


    public void updateEntryInDatabase(T newObject) throws IllegalAccessException {
        Field[] fields = daoClazz.getDeclaredFields();
        Document doc = new Document();
        for (Field field : fields) {
            field.setAccessible(true);
            doc.append(field.getName(), field.get(daoClazz).toString());
        }
        mongoCollection.updateOne(eq("id", doc.get("id")), doc);
    }


    public Document retrieveEntryById(String itemId) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("id", itemId);
        return getMongoCollection().find(searchQuery).first();
    }

    public void deleteEntryinDb(String itemId, MongoCollection<Document> collection) {
        collection.deleteOne(eq("id", itemId));
    }


}
