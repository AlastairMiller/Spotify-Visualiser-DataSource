package al.miller.cv.ds.dao;

import al.miller.cv.ds.ClientHandler;
import al.miller.cv.ds.mapper.RefinedtoJsonMapper;
import com.mongodb.client.MongoCollection;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.beans.ConstructorProperties;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

@Slf4j
@Data
public abstract class AbstractDao<T> implements DaoInterface<T> {

    @SuppressWarnings("unchecked")
    protected final Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    protected ClientHandler clientHandler;
    protected MongoCollection<T> mongoCollection;

    @ConstructorProperties({"clientHandler", "mongoCollectionName"})
    public AbstractDao(ClientHandler clientHandler, String collectionName) {
        this.clientHandler = clientHandler;
        this.mongoCollection = this.clientHandler.getMongoDB().getCollection(collectionName, clazz);
    }

    @Override
    public T getById(String id) {
        return mongoCollection.find(eq("id", id))
                .first();
    }

    @Override
    public List<T> getMultipleById(List<String> ids) {
        return mongoCollection.find(eq("id", ids))
                .into(new ArrayList<>());
    }

    @Override
    public T getBySpotifyUri(String spotifyId) {
        return mongoCollection.find(eq("spotifyURI", spotifyId))
                .first();
    }

    public List<T> getAllMatchingNames(String name) {
        return mongoCollection.find(eq("name", name))
                .into(new ArrayList<>());
    }

    public List<T> getMostPopular(int limit, int offset) {
        return mongoCollection.find()
                .sort(orderBy(descending("popularity")))
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