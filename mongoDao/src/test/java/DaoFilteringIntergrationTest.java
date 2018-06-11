import com.svd.ClientHandler;
import com.svd.dao.*;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import refinedDataModels.RefinedAlbum;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DaoFilteringIntergrationTest {
    private static final String DATABASE_NAME = "embedded";

    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private RefinedAlbumDao refinedAlbumDao;
    private RefinedArtistDao artistDao;
    private RefinedPlaylistDao playlistDao;
    private RefinedTrackDao trackDao;
    private RefinedUserDao userDao;

    private static final String hostname = "127.0.0.1";
    private static final int port = 12345;

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    @Before
    public void beforeEach() throws Exception {
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(hostname, port, Network.localhostIsIPv6()))
                .build();

        mongodExe = starter.prepare(mongodConfig);
        mongod = mongodExe.start();
        refinedAlbumDao = new RefinedAlbumDao(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedAlbums");
        artistDao = new RefinedArtistDao(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedArtists");
        playlistDao = new RefinedPlaylistDao(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedPlaylists");
        trackDao = new RefinedTrackDao(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedTracks");
        userDao = new RefinedUserDao(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedUsers");
    }

    @After
    public void afterEach() {
        if (this.mongod != null) {
            this.mongod.stop();
            this.mongodExe.stop();
        }
    }

    @Test
    public void shouldGetAlbumsFromArtistId() throws MalformedURLException {
        RefinedAlbum loveIsDead = RefinedAlbum.builder()
                .id("2hshVOA4ULsMGHPDiUaDbJ")
                .name("Love Is Dead")
                .artistsIds(Collections.singletonList("3CjlHNtplJyTf9npxaPl5w"))
                .trackIds(new ArrayList<>())
                .imageURL(new URL("https://i.scdn.co/image/23236f96e36c163099b4770b5aa4f04146b71a7e"))
                .releaseDate(new Date(1527202800000L))
                .popularity(61)
                .externalURL(new URL("https://open.spotify.com/artist/3CjlHNtplJyTf9npxaPl5w"))
                .href(new URL("https://api.spotify.com/v1/artists/3CjlHNtplJyTf9npxaPl5w"))
                .spotifyURI("spotify:album:2hshVOA4ULsMGHPDiUaDbJ")
                .build();

        RefinedAlbum everyOpenEye = RefinedAlbum.builder()
                .id("3vz4V1QW1DBzqvNKT1BMgg")
                .name("Every Open Eye (Extended Edition)")
                .artistsIds(Collections.singletonList("3CjlHNtplJyTf9npxaPl5w"))
                .trackIds(new ArrayList<>())
                .imageURL(new URL("https://i.scdn.co/image/573a8b598366a3fb17ba2fdf5ec0beda7ec72d19"))
                .releaseDate(new Date(1469746800000L))
                .popularity(48)
                .externalURL(new URL("https://open.spotify.com/album/3vz4V1QW1DBzqvNKT1BMgg"))
                .href(new URL("https://api.spotify.com/v1/albums/3vz4V1QW1DBzqvNKT1BMgg"))
                .spotifyURI("spotify:album:3vz4V1QW1DBzqvNKT1BMgg")
                .build();

        RefinedAlbum raw = RefinedAlbum.builder()
                .id("1VyusuPKHWr2fC5l77hi1L")
                .name("Raw")
                .artistsIds(Collections.singletonList("4TrraAsitQKl821DQY42cZ"))
                .trackIds(new ArrayList<>())
                .imageURL(new URL("https://i.scdn.co/image/ceb1f2d11f142684bbf2264c5d39242e41ca1f6e"))
                .releaseDate(new Date(1523574000000L))
                .popularity(63)
                .externalURL(new URL("https://open.spotify.com/album/1VyusuPKHWr2fC5l77hi1L"))
                .href(new URL("https://api.spotify.com/v1/albums/1VyusuPKHWr2fC5l77hi1L"))
                .spotifyURI("spotify:album:1VyusuPKHWr2fC5l77hi1L")
                .build();

        List<RefinedAlbum> albumsToPersist = new ArrayList<RefinedAlbum>() {{
            add(loveIsDead);
            add(everyOpenEye);
            add(raw);
        }};
        refinedAlbumDao.saveList(albumsToPersist);

        assertThat(refinedAlbumDao.getMongoCollection().count(), is(3L));

        List<RefinedAlbum> filteredForArtistChvrches = refinedAlbumDao.geAllfromArtistId(0,10,"3CjlHNtplJyTf9npxaPl5w");

        assertThat(filteredForArtistChvrches.size(), is(2));
        assertThat(filteredForArtistChvrches.get(0), is(loveIsDead));
        assertThat(filteredForArtistChvrches.get(1), is(everyOpenEye));

    }

    @Test
    public void shouldSortByNewest(){
        
    }
}
