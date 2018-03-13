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
import refinedDataModels.RefinedAlbum;
import refinedDataModels.RefinedArtist;
import refinedDataModels.RefinedTrack;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static com.svd.mapper.JsontoRefinedMapper.toArtist;
import static com.svd.mapper.JsontoRefinedMapper.toTrack;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class DaoIntegrationTest {
    private static final String DATABASE_NAME = "embedded";

    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private MongoClient mongoClient;
    private BaseDao mongoDao;
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
        mongoDao = new BaseDao<>(new ClientHandler(hostname, port, "embedded"));
    }

    @After
    public void afterEach() {
        if (this.mongod != null) {
            this.mongod.stop();
            this.mongodExe.stop();
        }
    }

    void saveExampleAlbumToDatabase(MongoCollection<Document> mongoCollection){
        RefinedAlbum exampleAlbum = new RefinedAlbum();
        try {
            exampleAlbum = RefinedAlbum.builder()
                    .id("6fpZzsdzd04nqiDPWnF2iw")
                    .name("All I Need (Deluxe Version)")
                    .artistsIds(Collections.singletonList("7qRll6DYV06u2VuRPAVqug"))
                    .trackIds(new ArrayList<String>(Arrays.asList("4GKmx23LpQNXkamXDoZyFE", "08pOoVfZ8BsLCfXxRsci2Y", "72nV5T9HAbKc1sLka9NF6x", "69Q9unT80s08dUolhcgr4T", "5bLgPkbqC97jVe6rJlpzLJ", "7b63AiSD37IYRJBrEQcfdR", "6iudA5joUv7hmPQYFIVEB5", "2SjYwtyKUTQQ8WoxX8uX9w", "1621HusJy3pKPrynU8nmB3", "3eDIItHQg5S69tfVR12RYh", "5gLCxvVic5y7PBLDzBsgmd", "63jqw4EEcCxUIqJm4514ZB", "2umo57mtmQOEoCza8OncR0", "1puaMbxVMGdZnPTk1rq19X", "1puaMbxVMGdZnPTk1rq19X", "27oi5r6fj4aPcV86Z40Wrm")))
                    .imageURL(new URL("https://i.scdn.co/image/fb49d9c64b1e8b2af45498ad23603a749cd1b177"))
                    .releaseDate(new Date(1454630400000L))
                    .popularity(61)
                    .externalURL(new URL("https://open.spotify.com/album/6fpZzsdzd04nqiDPWnF2iw"))
                    .href(new URL("https://api.spotify.com/v1/albums/6fpZzsdzd04nqiDPWnF2iw"))
                    .uri(new URI("spotify:album:6fpZzsdzd04nqiDPWnF2iw"))
                    .build();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
        mongoDao.saveEntryToDatabase(RefinedAlbum.class, exampleAlbum, mongoCollection);

    }

    void saveExampleTrackToDatabase(MongoCollection<Document> mongoCollection) {
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
                    .href(new URL("https://api.spotify.com/v1/tracks/3aTrurxagDJfsQRBEOGfMb"))
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

    void saveExampleArtistToDatabase(MongoCollection<Document> mongoCollection) {
        RefinedArtist exampleArtist = new RefinedArtist();
        try {
            exampleArtist = RefinedArtist.builder()
                    .id("5cIc3SBFuBLVxJz58W2tU9")
                    .externalURL(new URL("https://open.spotify.com/artist/5cIc3SBFuBLVxJz58W2tU9"))
                    .genres(new ArrayList<String>(Arrays.asList("indie poptimism", "indie r&b")))
                    .refinedAlbumIds(new ArrayList<String>(Arrays.asList(
                            "0zicd2mBV8HTzSubByj4vP", "562fzRzo7acnqGXGuty2az", "2x7GLQ1X785XAamwhzJUJG", "74mKRJaNS5PWUADvFJS7ji",
                            "733e1ZfktLSwj96X5rsMeE", "2wGl54RF5pLv212S4bEXtT", "2wqFdb5lFQVGiNP7kUETnC", "1Y5DV3pPPLmpClvFw8Dtcj",
                            "5GXrievxdXgabSPY9gVq5J", "6EH6qQ516EF4NUEl6FsWit", "6x8tzop53PDl2tvjIXB9a0", "7FJANHeAa8sEWVFio0evWX",
                            "1lpTHlvDpmR1LDE0ru6Cth", "4LA0h9eDJKoKfwiDWxGlFM", "4wl4BQZQocAFjggJ4nN70H", "0Z3ZIqbi57Xi9bEfL5JWQk",
                            "6dUOLPzICJ8Ap9hdY28AnA")))
                    .href(new URL("https://api.spotify.com/v1/artists/5cIc3SBFuBLVxJz58W2tU9"))
                    .name("Oh Wonder")
                    .followers(615216)
                    .popularity(78)
                    .uri(new URI("spotify:artist:5cIc3SBFuBLVxJz58W2tU9"))
                    .build();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
        mongoDao.saveEntryToDatabase(RefinedArtist.class, exampleArtist, mongoCollection);
    }

    @Test
    public void shouldSaveTrackToDb() {
        MongoCollection<Document> mongoCollection = mongoDao.getClientHandler().getMongoDB().getCollection("RefinedTracks");
        saveExampleTrackToDatabase(mongoCollection);
        assertThat(mongoCollection.count(), Matchers.is(1L));
    }

    @Test
    public void shouldReadSavedTrackFromDb() {
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
                    .href(new URL("https://api.spotify.com/v1/tracks/3aTrurxagDJfsQRBEOGfMb"))
                    .name("The Other Side Of Paradise")
                    .previewURL(new URL("https://p.scdn.co/mp3-preview/228a01d80735cc6f137f1a54c5b28122aa03123e?cid=8897482848704f2a8f8d7c79726a70d4"))
                    .trackNumber(8)
                    .popularity(60)
                    .uri(URI.create("spotify:track:3aTrurxagDJfsQRBEOGfMb"))
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        MongoCollection<Document> mongoCollection = mongoDao.getClientHandler().getMongoDB().getCollection("RefinedTracks");
        saveExampleTrackToDatabase(mongoCollection);
        Object object = mongoDao.retrieveEntryById("3aTrurxagDJfsQRBEOGfMb", "RefinedTracks");
        RefinedTrack actualSong = toTrack((Document) object);
        Assert.assertEquals(expectedSong, actualSong);

    }

    @Test
    public void shouldSaveArtistToDb() {
        MongoCollection<Document> mongoCollection = mongoDao.getClientHandler().getMongoDB().getCollection("RefinedArtists");
        saveExampleArtistToDatabase(mongoCollection);
        assertThat(mongoCollection.count(), Matchers.is(1L));
    }

    @Test
    public void shouldReadSavedArtistFromDb(){
        RefinedArtist expectedArtist = new RefinedArtist();
        try {
            expectedArtist = RefinedArtist.builder()
                    .id("5cIc3SBFuBLVxJz58W2tU9")
                    .externalURL(new URL("https://open.spotify.com/artist/5cIc3SBFuBLVxJz58W2tU9"))
                    .genres(new ArrayList<String>(Arrays.asList("indie poptimism", "indie r&b")))
                    .refinedAlbumIds(new ArrayList<String>(Arrays.asList(
                            "0zicd2mBV8HTzSubByj4vP", "562fzRzo7acnqGXGuty2az", "2x7GLQ1X785XAamwhzJUJG", "74mKRJaNS5PWUADvFJS7ji",
                            "733e1ZfktLSwj96X5rsMeE", "2wGl54RF5pLv212S4bEXtT", "2wqFdb5lFQVGiNP7kUETnC", "1Y5DV3pPPLmpClvFw8Dtcj",
                            "5GXrievxdXgabSPY9gVq5J", "6EH6qQ516EF4NUEl6FsWit", "6x8tzop53PDl2tvjIXB9a0", "7FJANHeAa8sEWVFio0evWX",
                            "1lpTHlvDpmR1LDE0ru6Cth", "4LA0h9eDJKoKfwiDWxGlFM", "4wl4BQZQocAFjggJ4nN70H", "0Z3ZIqbi57Xi9bEfL5JWQk",
                            "6dUOLPzICJ8Ap9hdY28AnA")))
                    .href(new URL("https://api.spotify.com/v1/artists/5cIc3SBFuBLVxJz58W2tU9"))
                    .name("Oh Wonder")
                    .followers(615216)
                    .popularity(78)
                    .uri(new URI("spotify:artist:5cIc3SBFuBLVxJz58W2tU9"))
                    .build();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
        MongoCollection<Document> mongoCollection = mongoDao.getClientHandler().getMongoDB().getCollection("RefinedArtists");
        saveExampleArtistToDatabase(mongoCollection);
        Object object = mongoDao.retrieveEntryById("5cIc3SBFuBLVxJz58W2tU9", "RefinedArtists");
        RefinedArtist actualArtist = toArtist((Document) object);
        Assert.assertEquals(expectedArtist, actualArtist);

    }


}