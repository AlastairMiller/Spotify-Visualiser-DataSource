package com.svd;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;

public class ClientHandler {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDB;

    public ClientHandler(String host, int port, String databaseName) {
        mongoClient = new MongoClient(host , port);
        mongoDB = mongoClient.getDatabase(databaseName);
    }

    public ClientHandler(String host, int port, String userName, String databaseName, char[] password){
        MongoCredential credential = MongoCredential.createCredential(userName, databaseName, password);
        mongoClient = new MongoClient(host , port);
        mongoDB = mongoClient.getDatabase(databaseName);
    }
}
