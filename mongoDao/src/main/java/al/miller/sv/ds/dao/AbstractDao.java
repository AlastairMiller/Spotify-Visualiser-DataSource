package al.miller.sv.ds.dao;

import al.miller.sv.ds.ClientHandler;
import al.miller.sv.ds.mapper.RefinedtoJsonMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.beans.ConstructorProperties;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.text;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

@Slf4j
@Data
@Repository
public abstract class AbstractDao<T> implements DaoInterface<T> {

    @SuppressWarnings("unchecked")
    protected final Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    protected ClientHandler clientHandler;
    protected MongoCollection<T> mongoCollection;
    protected String collectionName;

    @ConstructorProperties({"clientHandler", "mongoCollectionName"})
    public AbstractDao(ClientHandler clientHandler, String collectionName) {
        this.clientHandler = clientHandler;
        this.mongoCollection = this.clientHandler.getMongoDB().getCollection(collectionName, clazz);
        this.collectionName = collectionName;
    }

    @Override
    public void createCollection(String collectionName) {
        clientHandler.getMongoDB().createCollection(collectionName);
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return clientHandler.getMongoDB().getCollection(collectionName);
    }


    @Override
    public T getById(String id) {
        return mongoCollection.find(eq("id", id))
                .first();
    }


    @Override
    public T getRandom() {
        return mongoCollection.aggregate(Collections.singletonList(new BsonDocument("$sample", new BsonDocument("size", new BsonInt32(1))))).first();
    }

    @Override
    public List<T> getMultipleById(List<String> ids) {
        return mongoCollection.find(in("id", ids))
                .sort(Sorts.ascending("id"))
                .into(new ArrayList<>());
    }

    @Override
    public T getBySpotifyUri(String spotifyId) {
        return mongoCollection.find(eq("spotifyURI", spotifyId))
                .first();
    }

    public List<T> getAllMatchingNames(String name, int limit, int offset) {
        return mongoCollection.find(text(name))
                .skip(offset)
                .limit(limit)
                .into(new ArrayList<>());
    }

    public List<T> getMostPopular(int limit, int offset) {
        return mongoCollection.find()
                .sort(orderBy(descending("popularity")))
                .skip(offset)
                .limit(limit - offset)
                .into(new ArrayList<>());
    }

    @Override
    public void save(T inputObject) {
        mongoCollection.insertOne(inputObject);
    }

    @Override
    public void saveList(List<T> inputList) {
        mongoCollection.insertMany(inputList);
    }

    @Override
    public void update(String id, T newObject) {
        Document document = convertObjectToDocument(newObject);
        mongoCollection.findOneAndUpdate(eq("id", id), document);
    }

    @Override
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
                document.append(field.getName(), RefinedtoJsonMapper.invokeSimpleGetter(getter, inputObject));
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage());
            }
        }
        return document;
    }
}
