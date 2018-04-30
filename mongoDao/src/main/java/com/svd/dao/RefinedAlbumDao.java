package com.svd.dao;

import com.mongodb.client.MongoCollection;
import com.svd.ClientHandler;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@Slf4j
public class RefinedAlbumDao implements MongoDao {

    private ClientHandler clientHandler;

    RefinedAlbumDao(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return null;
    }

    public void saveEntryToDatabase(Class inputClass, Object inputObject, MongoCollection<Document> collection) {

    }


    public void updateEntryInDatabase(Class inputObject, MongoCollection<Document> collection) throws IllegalAccessException {

    }


    public Document retrieveEntryById(String itemId, String collectionName) {
        return null;
    }

    public void deleteEntryinDb(String itemId, MongoCollection<Document> collection) {

    }


}
