import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.svd.ClientHandler;
import com.svd.dao.BaseDao;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import refinedDataModels.RefinedTrack;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;

import static com.svd.mapper.JsontoRefinedMapper.toTrack;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class DaoIntegrationTest {
    private static final String DATABASE_NAME = "embedded";

    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private MongoClient mongoClient;
    private BaseDao<RefinedTrack> mongoDao;
    private String hostname = "127.0.0.1";
    private int port = 12345;

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    @Before
    public void beforeEach() throws Exception {
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(hostname, port, Network.localhostIsIPv6()))
                .build();

        mongodExe = starter.prepare(mongodConfig);
        mongod = mongodExe.start();
        mongoClient = new MongoClient(hostname, port);
        mongoDao = new BaseDao<RefinedTrack>(new ClientHandler(hostname, port, "embedded"));
    }

    @After
    public void afterEach() {
        if (this.mongod != null) {
            this.mongod.stop();
            this.mongodExe.stop();
        }
    }

    void saveExampleSongToDatabase(MongoCollection<org.bson.Document> mongoCollection) {
        RefinedTrack exampleTrack = new RefinedTrack();
        try {
            exampleTrack = RefinedTrack.builder()
                    .id("3aTrurxagDJfsQRBEOGfMb")
                    .refinedAlbumIds(Collections.singletonList("24BRvmlDhVhjTJsqazdVxm"))
                    .refinedArtistIds(Collections.singletonList("4yvcSjfu4PC0CYQyLy4wSq"))
                    .discNum(1)
                    .durationMs(320654)
                    .explicit(true)
                    .externalURL(new URL("https://open.spotify.com/track/3aTrurxagDJfsQRBEOGfMb"))
                    .href("https://api.spotify.com/v1/tracks/3aTrurxagDJfsQRBEOGfMb")
                    .name("The Other Side Of Paradise")
                    .previewURL(new URL("https://p.scdn.co/mp3-preview/228a01d80735cc6f137f1a54c5b28122aa03123e?cid=8897482848704f2a8f8d7c79726a70d4"))
                    .trackNumber(8)
                    .popularity(60)
                    .uri(URI.create("spotify:track:3aTrurxagDJfsQRBEOGfMb"))
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mongoDao.saveEntryToDatabase(RefinedTrack.class, exampleTrack, mongoCollection);
    }

    @Test
    public void shouldSaveTracktoDb() {
        MongoCollection<org.bson.Document> mongoCollection = mongoDao.getClientHandler().getMongoDB().getCollection("RefinedTracks");
        saveExampleSongToDatabase(mongoCollection);
        assertThat(mongoCollection.count(), Matchers.is(1L));
    }


    @Test
    public void shouldReadTrackFromDb() {
        RefinedTrack expectedSong = new RefinedTrack();
        try {
            expectedSong = RefinedTrack.builder()
                    .id("3aTrurxagDJfsQRBEOGfMb")
                    .refinedAlbumIds(Collections.singletonList("24BRvmlDhVhjTJsqazdVxm"))
                    .refinedArtistIds(Collections.singletonList("4yvcSjfu4PC0CYQyLy4wSq"))
                    .discNum(1)
                    .durationMs(320654)
                    .explicit(true)
                    .externalURL(new URL("https://open.spotify.com/track/3aTrurxagDJfsQRBEOGfMb"))
                    .href("https://api.spotify.com/v1/tracks/3aTrurxagDJfsQRBEOGfMb")
                    .name("The Other Side Of Paradise")
                    .previewURL(new URL("https://p.scdn.co/mp3-preview/228a01d80735cc6f137f1a54c5b28122aa03123e?cid=8897482848704f2a8f8d7c79726a70d4"))
                    .trackNumber(8)
                    .popularity(60)
                    .uri(URI.create("spotify:track:3aTrurxagDJfsQRBEOGfMb"))
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        MongoCollection<org.bson.Document> mongoCollection = mongoDao.getClientHandler().getMongoDB().getCollection("RefinedTracks");
        saveExampleSongToDatabase(mongoCollection);
        long as = mongoCollection.count();
        Object object = mongoDao.retrieveEntryById("3aTrurxagDJfsQRBEOGfMb", "RefinedTracks");
        RefinedTrack actualSong = toTrack((Document) object);
        Assert.assertEquals(expectedSong, actualSong);

    }

}
