package com.svd.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.svd.ClientHandler;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;


abstract class BaseDao<T> {

    private final ClientHandler clientHandler;

    BaseDao(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    MongoCollection<Document> getCollection(String collectionName) {

        return clientHandler.getMongoDB().getCollection(collectionName);
    }


    void saveEntryToDatabase(Class<T> inputObject, MongoCollection<Document> collection) throws IllegalAccessException {
        List<Field> fields = Arrays.asList(inputObject.getDeclaredFields());
        Document doc = new Document();
        for (Field field : fields) {
            field.setAccessible(true);
            doc.append(field.getName(), field.get(inputObject).toString());
        }
        collection.insertOne(doc);
    }


    void updateEntryInDatabase(Class<T> inputObject, MongoCollection<Document> collection) throws IllegalAccessException {
        List<Field> fields = Arrays.asList(inputObject.getDeclaredFields());
        Document doc = new Document();
        for (Field field : fields) {
            field.setAccessible(true);
            doc.append(field.getName(), field.get(inputObject).toString());
        }
        collection.updateOne(doc, doc);
    }


    Object retrieveEntryById(String itemId, String collectionName) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("id", itemId);
        Document doc = getCollection(collectionName).find(searchQuery).first();
        //map
        return doc;
    }

    void deleteEntryinDb(String itemId, MongoCollection<Document> collection){
        collection.deleteOne(eq("id",itemId));
    }


}
