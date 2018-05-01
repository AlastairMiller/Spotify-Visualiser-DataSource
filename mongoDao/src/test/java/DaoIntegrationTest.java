import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.svd.ClientHandler;
import com.svd.dao.MongoDao;
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
import refinedDataModels.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static com.svd.mapper.JsontoRefinedMapper.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class DaoIntegrationTest {
    private static final String DATABASE_NAME = "embedded";

    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private MongoDao<RefinedAlbum> albumDao;
    private MongoDao<RefinedArtist> artistDao;
    private MongoDao<RefinedPlaylist> playlistDao;
    private MongoDao<RefinedTrack> trackDao;
    private MongoDao<RefinedUser> userDao;

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
        albumDao = new MongoDao<>(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedAlbums", RefinedAlbum.class);
        artistDao = new MongoDao<>(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedArtists", RefinedArtist.class);
        playlistDao = new MongoDao<>(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedPlaylists", RefinedPlaylist.class);
        trackDao = new MongoDao<>(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedTracks", RefinedTrack.class);
        userDao = new MongoDao<>(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedUsers", RefinedUser.class);
    }

    @After
    public void afterEach() {
        if (this.mongod != null) {
            this.mongod.stop();
            this.mongodExe.stop();
        }
    }

    private void saveExampleAlbumToDatabase(MongoCollection<Document> mongoCollection) throws MalformedURLException {
        RefinedAlbum exampleAlbum = RefinedAlbum.builder()
                .id("6fpZzsdzd04nqiDPWnF2iw")
                .name("All I Need (Deluxe Version)")
                .artistsIds(Collections.singletonList("7qRll6DYV06u2VuRPAVqug"))
                .trackIds(new ArrayList<String>(Arrays.asList(
                        "4GKmx23LpQNXkamXDoZyFE", "08pOoVfZ8BsLCfXxRsci2Y", "72nV5T9HAbKc1sLka9NF6x",
                        "69Q9unT80s08dUolhcgr4T", "5bLgPkbqC97jVe6rJlpzLJ", "7b63AiSD37IYRJBrEQcfdR",
                        "6iudA5joUv7hmPQYFIVEB5", "2SjYwtyKUTQQ8WoxX8uX9w", "1621HusJy3pKPrynU8nmB3",
                        "3eDIItHQg5S69tfVR12RYh", "5gLCxvVic5y7PBLDzBsgmd", "63jqw4EEcCxUIqJm4514ZB",
                        "2umo57mtmQOEoCza8OncR0", "1puaMbxVMGdZnPTk1rq19X", "1puaMbxVMGdZnPTk1rq19X",
                        "27oi5r6fj4aPcV86Z40Wrm")))
                .imageURL(new URL("https://i.scdn.co/image/fb49d9c64b1e8b2af45498ad23603a749cd1b177"))
                .releaseDate(new Date(1454630400000L))
                .popularity(61)
                .externalURL(new URL("https://open.spotify.com/album/6fpZzsdzd04nqiDPWnF2iw"))
                .href(new URL("https://api.spotify.com/v1/albums/6fpZzsdzd04nqiDPWnF2iw"))
                .spotifyURI("spotify:album:6fpZzsdzd04nqiDPWnF2iw")
                .build();

        albumDao.saveEntryToDatabase(exampleAlbum);

    }

    private void saveExampleTrackToDatabase(MongoCollection<Document> mongoCollection) throws MalformedURLException {
        RefinedTrack exampleTrack = RefinedTrack.builder()
                .id("3aTrurxagDJfsQRBEOGfMb")
                .albumId("24BRvmlDhVhjTJsqazdVxm")
                .artistIds(Collections.singletonList("4yvcSjfu4PC0CYQyLy4wSq"))
                .discNumber(1)
                .durationMs(320654)
                .explicit(true)
                .externalURL(new URL("https://open.spotify.com/track/3aTrurxagDJfsQRBEOGfMb"))
                .href(new URL("https://api.spotify.com/v1/tracks/3aTrurxagDJfsQRBEOGfMb"))
                .name("The Other Side Of Paradise")
                .previewURL(new URL("https://p.scdn.co/mp3-preview/228a01d80735cc6f137f1a54c5b28122aa03123e?cid=8897482848704f2a8f8d7c79726a70d4"))
                .trackNumber(8)
                .popularity(60)
                .spotifyURI("spotify:track:3aTrurxagDJfsQRBEOGfMb")
                .build();

        trackDao.saveEntryToDatabase(exampleTrack);
    }

    private void saveExampleArtistToDatabase(MongoCollection<Document> mongoCollection) throws MalformedURLException {
        RefinedArtist exampleArtist = RefinedArtist.builder()
                    .id("5cIc3SBFuBLVxJz58W2tU9")
                    .externalURL(new URL("https://open.spotify.com/artist/5cIc3SBFuBLVxJz58W2tU9"))
                    .genres(new ArrayList<>(Arrays.asList("indie poptimism", "indie r&b")))
                    .href(new URL("https://api.spotify.com/v1/artists/5cIc3SBFuBLVxJz58W2tU9"))
                    .name("Oh Wonder")
                    .followers(615216)
                    .popularity(78)
                    .spotifyURI("spotify:artist:5cIc3SBFuBLVxJz58W2tU9")
                    .build();

        artistDao.saveEntryToDatabase(exampleArtist);
    }

    private void saveExamplePlaylistToDatabase(MongoCollection<Document> mongoCollection) throws MalformedURLException {
        RefinedPlaylist examplePlaylist = RefinedPlaylist.builder()
                    .id("2CTdEa3JWbncC1h8WjnuxZ")
                    .externalURL(new URL("https://open.spotify.com/user/millersinc"))
                    .numOfFollowers(0)
                    .href(new URL("https://api.spotify.com/v1/users/millersinc"))
                    .imageURL(new URL("https://pl.scdn.co/images/pl/default/27eee5eb5ba85317586d3af6709bcebfc8525d83"))
                    .name("SPS Test playlist")
                    .description("&lt;Test&gt;")
                    .userId("millersinc")
                    .trackIds(new ArrayList<String>(Arrays.asList(
                            "5V3ZQQtWehePZs2ztZvyAi", "2T5cJy6jrHaciEUExBvxs8", "5dKyZWlgjWw1oJgLa4GCZD",
                            "5owRsFtcu8vxXYHvNyqdRr", "5aAx2yezTd8zXrkmtKl66Z")))
                    .spotifyURI("spotify:user:millersinc")
                    .build();

        playlistDao.saveEntryToDatabase(examplePlaylist);
    }

    private void saveExampleUserToDatabase(MongoCollection<Document> mongoCollection) throws MalformedURLException {
        RefinedUser exampleUser = RefinedUser.builder()
                    .id("spotify")
                    .displayName("Spotify")
                    .externalURL(new URL("https://open.spotify.com/user/spotify"))
                    .numOfFollowers(7715336)
                    .href(new URL("https://api.spotify.com/v1/users/spotify"))
                    .imageURL(new URL("https://profile-images.scdn.co/images/userprofile/default/3c93d52857ecf3e40c4e8435adb7f9c1da40a0dd"))
                    .spotifyURI("spotify:user:spotify")
                    .build();

        userDao.saveEntryToDatabase(exampleUser);
    }

    @Test
    public void shouldSaveAlbumToDb() throws MalformedURLException {
        MongoCollection<Document> mongoCollection = albumDao.getMongoCollection();
        saveExampleAlbumToDatabase(mongoCollection);
        assertThat(mongoCollection.count(), Matchers.is(1L));
    }

    @Test
    public void shouldReadSavedAlbumFromDb() throws MalformedURLException {
        RefinedAlbum expectedAlbum = RefinedAlbum.builder()
                    .id("6fpZzsdzd04nqiDPWnF2iw")
                    .name("All I Need (Deluxe Version)")
                    .artistsIds(Collections.singletonList("7qRll6DYV06u2VuRPAVqug"))
                    .trackIds(new ArrayList<String>(Arrays.asList(
                            "4GKmx23LpQNXkamXDoZyFE", "08pOoVfZ8BsLCfXxRsci2Y", "72nV5T9HAbKc1sLka9NF6x",
                            "69Q9unT80s08dUolhcgr4T", "5bLgPkbqC97jVe6rJlpzLJ", "7b63AiSD37IYRJBrEQcfdR",
                            "6iudA5joUv7hmPQYFIVEB5", "2SjYwtyKUTQQ8WoxX8uX9w", "1621HusJy3pKPrynU8nmB3",
                            "3eDIItHQg5S69tfVR12RYh", "5gLCxvVic5y7PBLDzBsgmd", "63jqw4EEcCxUIqJm4514ZB",
                            "2umo57mtmQOEoCza8OncR0", "1puaMbxVMGdZnPTk1rq19X", "1puaMbxVMGdZnPTk1rq19X",
                            "27oi5r6fj4aPcV86Z40Wrm")))
                    .imageURL(new URL("https://i.scdn.co/image/fb49d9c64b1e8b2af45498ad23603a749cd1b177"))
                    .releaseDate(new Date(1454630400000L))
                    .popularity(61)
                    .externalURL(new URL("https://open.spotify.com/album/6fpZzsdzd04nqiDPWnF2iw"))
                    .href(new URL("https://api.spotify.com/v1/albums/6fpZzsdzd04nqiDPWnF2iw"))
                    .spotifyURI("spotify:album:6fpZzsdzd04nqiDPWnF2iw")
                    .build();

        MongoCollection<Document> mongoCollection = albumDao.getMongoCollection();
        saveExampleAlbumToDatabase(mongoCollection);
        Object object = albumDao.retrieveEntryById("6fpZzsdzd04nqiDPWnF2iw");
        RefinedAlbum actualAlbum = toAlbum((Document) object);
        Assert.assertEquals(expectedAlbum, actualAlbum);
    }

    @Test
    public void shouldSaveTrackToDb() throws MalformedURLException {
        MongoCollection<Document> mongoCollection = trackDao.getMongoCollection();
        saveExampleTrackToDatabase(mongoCollection);
        assertThat(mongoCollection.count(), Matchers.is(1L));
    }

    @Test
    public void shouldReadSavedTrackFromDb() throws MalformedURLException {
        RefinedTrack expectedSong = RefinedTrack.builder()
                    .id("3aTrurxagDJfsQRBEOGfMb")
                    .albumId("24BRvmlDhVhjTJsqazdVxm")
                    .artistIds(Collections.singletonList("4yvcSjfu4PC0CYQyLy4wSq"))
                    .discNumber(1)
                    .durationMs(320654)
                    .explicit(true)
                    .externalURL(new URL("https://open.spotify.com/track/3aTrurxagDJfsQRBEOGfMb"))
                    .href(new URL("https://api.spotify.com/v1/tracks/3aTrurxagDJfsQRBEOGfMb"))
                    .name("The Other Side Of Paradise")
                    .previewURL(new URL("https://p.scdn.co/mp3-preview/228a01d80735cc6f137f1a54c5b28122aa03123e?cid=8897482848704f2a8f8d7c79726a70d4"))
                    .trackNumber(8)
                    .popularity(60)
                    .spotifyURI("spotify:track:3aTrurxagDJfsQRBEOGfMb")
                    .build();

        MongoCollection<Document> mongoCollection = trackDao.getMongoCollection();
        saveExampleTrackToDatabase(mongoCollection);
        Object object = trackDao.retrieveEntryById("3aTrurxagDJfsQRBEOGfMb");
        RefinedTrack actualSong = toTrack((Document) object);
        Assert.assertEquals(expectedSong, actualSong);
    }

    @Test
    public void shouldSaveArtistToDb() throws MalformedURLException {
        MongoCollection<Document> mongoCollection = artistDao.getMongoCollection();
        saveExampleArtistToDatabase(mongoCollection);
        assertThat(mongoCollection.count(), Matchers.is(1L));
    }

    @Test
    public void shouldReadSavedArtistFromDb() throws MalformedURLException {
        RefinedArtist expectedArtist = RefinedArtist.builder()
                    .id("5cIc3SBFuBLVxJz58W2tU9")
                    .externalURL(new URL("https://open.spotify.com/artist/5cIc3SBFuBLVxJz58W2tU9"))
                    .genres(new ArrayList<>(Arrays.asList("indie poptimism", "indie r&b")))
                    .href(new URL("https://api.spotify.com/v1/artists/5cIc3SBFuBLVxJz58W2tU9"))
                    .name("Oh Wonder")
                    .followers(615216)
                    .popularity(78)
                    .spotifyURI("spotify:artist:5cIc3SBFuBLVxJz58W2tU9")
                    .build();

        MongoCollection<Document> mongoCollection = artistDao.getMongoCollection();
        saveExampleArtistToDatabase(mongoCollection);
        Object object = artistDao.retrieveEntryById("5cIc3SBFuBLVxJz58W2tU9");
        RefinedArtist actualArtist = toArtist((Document) object);
        Assert.assertEquals(expectedArtist, actualArtist);
    }

    @Test
    public void shouldSavePlaylistToDb() throws MalformedURLException {
        MongoCollection<Document> mongoCollection = playlistDao.getMongoCollection();
        saveExamplePlaylistToDatabase(mongoCollection);
        assertThat(mongoCollection.count(), Matchers.is(1L));
    }

    @Test
    public void shouldReadSavedPlaylistFromDb() throws MalformedURLException {
        RefinedPlaylist expectedPlaylist = RefinedPlaylist.builder()
                    .id("2CTdEa3JWbncC1h8WjnuxZ")
                    .externalURL(new URL("https://open.spotify.com/user/millersinc"))
                    .numOfFollowers(0)
                    .href(new URL("https://api.spotify.com/v1/users/millersinc"))
                    .imageURL(new URL("https://pl.scdn.co/images/pl/default/27eee5eb5ba85317586d3af6709bcebfc8525d83"))
                    .name("SPS Test playlist")
                    .description("&lt;Test&gt;")
                    .userId("millersinc")
                    .trackIds(new ArrayList<String>(Arrays.asList(
                            "5V3ZQQtWehePZs2ztZvyAi", "2T5cJy6jrHaciEUExBvxs8", "5dKyZWlgjWw1oJgLa4GCZD",
                            "5owRsFtcu8vxXYHvNyqdRr", "5aAx2yezTd8zXrkmtKl66Z")))
                    .spotifyURI("spotify:user:millersinc")
                    .build();

        MongoCollection<Document> mongoCollection = playlistDao.getMongoCollection();
        saveExamplePlaylistToDatabase(mongoCollection);
        Object object = playlistDao.retrieveEntryById("2CTdEa3JWbncC1h8WjnuxZ");
        RefinedPlaylist actualPlaylist = toPlaylist((Document) object);
        Assert.assertEquals(expectedPlaylist, actualPlaylist);
    }

    @Test
    public void shouldSaveUserToDb() throws MalformedURLException {
        MongoCollection<Document> mongoCollection = userDao.getMongoCollection();
        saveExampleUserToDatabase(mongoCollection);
        assertThat(mongoCollection.count(), Matchers.is(1L));
    }

    @Test
    public void shouldReadSavedUserFromDb() throws MalformedURLException {
        RefinedUser expectedUser = RefinedUser.builder()
                .id("spotify")
                .displayName("Spotify")
                .externalURL(new URL("https://open.spotify.com/user/spotify"))
                .numOfFollowers(7715336)
                .href(new URL("https://api.spotify.com/v1/users/spotify"))
                .imageURL(new URL("https://profile-images.scdn.co/images/userprofile/default/3c93d52857ecf3e40c4e8435adb7f9c1da40a0dd"))
                .spotifyURI("spotify:user:spotify")
                .build();

        MongoCollection<Document> mongoCollection = userDao.getMongoCollection();
        saveExampleUserToDatabase(mongoCollection);
        Object object = userDao.retrieveEntryById("spotify");
        RefinedUser actualUser = toUser((Document) object);
        Assert.assertEquals(expectedUser, actualUser);
    }

}
