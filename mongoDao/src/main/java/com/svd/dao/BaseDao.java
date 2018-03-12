package com.svd.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.svd.ClientHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
@Slf4j
@Data
public class BaseDao<T> {

    private final ClientHandler clientHandler;

    public BaseDao(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    MongoCollection<Document> getCollection(String collectionName) {

        return clientHandler.getMongoDB().getCollection(collectionName);
    }

    public void saveEntryToDatabase(Class<T> inputClass, Object inputObject, MongoCollection<Document> collection){
        List<Field> fields = Arrays.asList(inputClass.getDeclaredFields());
        Document doc = new Document();
        for (Field field : fields) {
            field.setAccessible(true);
            Method getter = null;
            try {
                getter = inputClass.getDeclaredMethod("get{}", inputClass);
                doc.append(field.getName(), getter.invoke(inputObject));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                log.error("Error Mapping");
            }
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
        collection.updateOne(eq("id", doc.get("id")), doc);
    }


   public Object retrieveEntryById(String itemId, String collectionName) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("id", itemId);
        return getCollection(collectionName).find(searchQuery).first();
    }

    void deleteEntryinDb(String itemId, MongoCollection<Document> collection) {
        collection.deleteOne(eq("id", itemId));
    }


}
