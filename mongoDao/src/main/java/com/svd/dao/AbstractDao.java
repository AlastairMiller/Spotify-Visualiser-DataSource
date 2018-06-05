package com.svd.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.svd.ClientHandler;
import org.bson.BsonDocument;
import org.bson.BsonString;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractDao<T> {

    @SuppressWarnings("unchecked")
    private Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private ClientHandler clientHandler;
    private MongoCollection<T> mongoCollection;

    public List<T> getMostPopular(int limit, int offset) {
        FindIterable<T> tsByPopularity = mongoCollection.find().sort(new BsonDocument("popularity", new BsonString("-1")));
        // convert to List
        return tsByPopularity;
    }
}
