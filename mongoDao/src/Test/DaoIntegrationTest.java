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

import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class DaoIntegrationTest {
    private static final String DATABASE_NAME = "embedded";

    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private MongoClient mongoClient;
    private BaseDao mongoDao;

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    @Before
    public void beforeEach() throws Exception {
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net("localhost", 12345, Network.localhostIsIPv6()))
                .build();

        mongodExe = starter.prepare(mongodConfig);
        mongoClient = new MongoClient("localhost", 12345);
        mongoDao = new BaseDao(new ClientHandler("127.0.0.1", 12345, "embedded"));
    }

    @After
    public void afterEach() {
        if (this.mongod != null) {
            this.mongod.stop();
            this.mongodExe.stop();
        }
    }

    @Test
    public void shouldSaveTracktoDb() {
        MongoCollection mongoCollection = mongoDao.getClientHandler().getMongoDB().getCollection("RefinedTracks");
        RefinedTrack exampleTrack = new RefinedTrack();
        try {
            exampleTrack = RefinedTrack.builder()
                        .id("3aTrurxagDJfsQRBEOGfMb")
                        .refinedAlbumsIds(Collections.singletonList("24BRvmlDhVhjTJsqazdVxm"))
                        .refinedArtistsIds(Collections.singletonList("4yvcSjfu4PC0CYQyLy4wSq"))
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
        mongoDao.saveEntryToDatabase(RefinedTrack.class ,exampleTrack,  mongoCollection);
        assertThat(mongoCollection.count(), Matchers.is(1L));
    }


    @Test
    public void shouldReadTrackFromDb() {
        RefinedTrack exampleSong = new RefinedTrack();
        try {
             exampleSong = RefinedTrack.builder()
                    .id("3aTrurxagDJfsQRBEOGfMb")
                    .refinedAlbumsIds(Collections.singletonList("24BRvmlDhVhjTJsqazdVxm"))
                    .refinedArtistsIds(Collections.singletonList("4yvcSjfu4PC0CYQyLy4wSq"))
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
        RefinedTrack actualSong = (RefinedTrack) mongoDao.retrieveEntryById("3aTrurxagDJfsQRBEOGfMb", "RefinedTracks");
        Assert.assertEquals(exampleSong,actualSong);

    }

}
