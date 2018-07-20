package al.miller.sv.ds;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import fr.javatic.mongo.jacksonCodec.JacksonCodecProvider;
import lombok.Data;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import static fr.javatic.mongo.jacksonCodec.ObjectMapperFactory.createObjectMapper;

@Data
public class ClientHandler {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDB;

    public ClientHandler(MongoClient mongoClient, MongoDatabase mongoDB){
        this.mongoClient = mongoClient;
        this.mongoDB = mongoDB;
    }

    public ClientHandler(String host, int port, String databaseName) {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(new JacksonCodecProvider(createObjectMapper())));
        MongoClientOptions clientOptions = MongoClientOptions.builder()
                .codecRegistry(codecRegistry)
                .build();

        ServerAddress serverAddress = new ServerAddress(host,port);
        mongoClient = new MongoClient(serverAddress, clientOptions);
        mongoDB = mongoClient.getDatabase(databaseName);

    }

    public ClientHandler(String host, int port, String userName, String databaseName, char[] password) {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(new JacksonCodecProvider(createObjectMapper())));
        MongoClientOptions clientOptions = MongoClientOptions.builder()
                .codecRegistry(codecRegistry)
                .build();

        ServerAddress serverAddress = new ServerAddress(host,port);
        MongoCredential credential = MongoCredential.createCredential(userName, databaseName, password);
        mongoClient = new MongoClient(serverAddress, credential, clientOptions);
        mongoDB = mongoClient.getDatabase(databaseName);
    }
}
