package com.svd.dao;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public interface MongoDao {

    MongoCollection<Document> getCollection(String collectionName);

    void saveEntryToDatabase(Class inputClass, Object inputObject, MongoCollection<Document> collection);


    void updateEntryInDatabase(Class inputObject, MongoCollection<Document> collection) throws IllegalAccessException;


    Document retrieveEntryById(String itemId, String collectionName);

    void deleteEntryinDb(String itemId, MongoCollection<Document> collection);


}
