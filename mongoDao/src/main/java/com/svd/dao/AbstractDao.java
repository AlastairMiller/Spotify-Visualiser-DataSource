package com.svd.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.svd.ClientHandler;
import org.bson.BsonDocument;
import org.bson.BsonString;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> {

    @SuppressWarnings("unchecked")
    private Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private ClientHandler clientHandler;
    private MongoCollection<T> mongoCollection;

    public T getById(String id){
        BasicDBObject query = new BasicDBObject("id", id);
        return mongoCollection.find(query).first();
    }

    public List<T> getAllMatchingNames(String name){
        BasicDBObject query = new BasicDBObject("name", name);
        return mongoCollection.find(query).into(new ArrayList<>());
    }

    public List<T> getMostPopular(int limit, int offset) {
        FindIterable<T> tsByPopularity = mongoCollection.find().sort(new BsonDocument("popularity", new BsonString("-1")));
        // convert to List
        return tsByPopularity;
    }

    public void save(T inputObject){
        mongoCollection.insertOne(inputObject);
    }

    public void update(T inputObject){

    }
}
