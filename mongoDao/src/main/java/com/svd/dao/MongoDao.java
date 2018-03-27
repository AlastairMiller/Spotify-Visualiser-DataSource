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

import static com.mongodb.client.model.Filters.eq;
import static com.svd.mapper.RefinedtoJsonMapper.invokeSimpleGetter;

@Slf4j
@Data
public class MongoDao<T> {

    private final ClientHandler clientHandler;

    public MongoDao(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public MongoCollection<Document> getCollection(String collectionName) {

        return clientHandler.getMongoDB().getCollection(collectionName);
    }

    public void saveEntryToDatabase(Class<T> inputClass, Object inputObject, MongoCollection<Document> collection) {
        List<Field> fields = Arrays.asList(inputClass.getDeclaredFields());
        Document doc = new Document();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Method getter;
                if (field.getType().getName().equals("boolean")) {
                    getter = inputClass.getDeclaredMethod("is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                } else {
                    getter = inputClass.getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                }
                doc.append(field.getName(), invokeSimpleGetter(getter, inputObject));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        collection.insertOne(doc);
    }


    public void updateEntryInDatabase(Class<T> inputObject, MongoCollection<Document> collection) throws IllegalAccessException {
        List<Field> fields = Arrays.asList(inputObject.getDeclaredFields());
        Document doc = new Document();
        for (Field field : fields) {
            field.setAccessible(true);
            doc.append(field.getName(), field.get(inputObject).toString());
        }
        collection.updateOne(eq("id", doc.get("id")), doc);
    }


    public Document retrieveEntryById(String itemId, String collectionName) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("id", itemId);
        return getCollection(collectionName).find(searchQuery).first();
    }

    public void deleteEntryinDb(String itemId, MongoCollection<Document> collection) {
        collection.deleteOne(eq("id", itemId));
    }


}
