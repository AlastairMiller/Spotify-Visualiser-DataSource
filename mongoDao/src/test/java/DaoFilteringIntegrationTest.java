import com.svd.ClientHandler;
import com.svd.dao.*;
import com.svd.util.SortOrder;
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
import refinedDataModels.RefinedArtist;
import refinedDataModels.RefinedPlaylist;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DaoFilteringIntegrationTest {
    private static final String DATABASE_NAME = "embedded";

    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private RefinedAlbumDao refinedAlbumDao;
    private RefinedArtistDao refinedArtistDao;
    private RefinedPlaylistDao refinedPlaylistDao;
    private RefinedTrackDao refinedTrackDao;
    private RefinedUserDao refinedUserDao;

    private static final String hostname = "127.0.0.1";
    private static final int port = 12345;

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    private final RefinedAlbum everyOpenEye = RefinedAlbum.builder()
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

    private final RefinedAlbum loveIsDead = RefinedAlbum.builder()
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

    private final RefinedAlbum raw = RefinedAlbum.builder()
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

    private final RefinedArtist nothingButThieves = RefinedArtist.builder()
            .id("1kDGbuxWknIKx4FlgWxiSp")
            .externalURL(new URL("https://open.spotify.com/artist/1kDGbuxWknIKx4FlgWxiSp"))
            .genres(new ArrayList<String>() {{
                add("alternative pop rock");
                add("modern alternative rock");
                add("modern rock");
            }})
            .href(new URL("https://api.spotify.com/v1/artists/1kDGbuxWknIKx4FlgWxiSp"))
            .name("Nothing But Thieves")
            .followers(328203)
            .popularity(68)
            .spotifyURI("spotify:artist:1kDGbuxWknIKx4FlgWxiSp")
            .build();

    private final RefinedArtist theAmazons = RefinedArtist.builder()
            .id("7243txmysJ4KbRmH8UAMKO")
            .externalURL(new URL("https://open.spotify.com/artist/7243txmysJ4KbRmH8UAMKO"))
            .genres(new ArrayList<String>() {{
                add("alt-indie rock");
                add("indie rock");
                add("modern alternative rock");
                add("modern rock");
            }})
            .href(new URL("https://api.spotify.com/v1/artists/7243txmysJ4KbRmH8UAMKO\""))
            .name("The Amazons")
            .followers(39891)
            .popularity(52)
            .spotifyURI("spotify:artist:7243txmysJ4KbRmH8UAMKO")
            .build();

    private final RefinedArtist walkTheMoon = RefinedArtist.builder()
            .id("WALK THE MOON")
            .externalURL(new URL("https://open.spotify.com/artist/6DIS6PRrLS3wbnZsf7vYic"))
            .genres(new ArrayList<String>() {{
                add("indie pop");
                add("indie poptimism");
                add("modern rock");
                add("pop");
            }})
            .href(new URL("https://api.spotify.com/v1/artists/6DIS6PRrLS3wbnZsf7vYic"))
            .name("WALK THE MOON")
            .followers(932990)
            .popularity(73)
            .spotifyURI("spotify:artist:6DIS6PRrLS3wbnZsf7vYic")
            .build();

    private final RefinedPlaylist datasourceTestPlaylist1 = RefinedPlaylist.builder()
            .id("6NEs2Ky3g64uhhpcjJWZPv")
            .externalURL(new URL("https://open.spotify.com/user/millersinc/playlist/6NEs2Ky3g64uhhpcjJWZPv"))
            .numOfFollowers(0)
            .href(new URL("https://api.spotify.com/v1/users/millersinc/playlists/6NEs2Ky3g64uhhpcjJWZPv"))
            .imageURL(new URL("https://i.scdn.co/image/aff27e3ce2b5047d009405c292c64b3ccf9d629f"))
            .name("Datasource Test Playlist 1")
            .description("Test 1")
            .userId("millersinc")
            .trackIds(new ArrayList<String>() {{
                add("20CNpCKq1oTdvekXaboyeq");
                add("2JO3HwMRPeya8bXbtbyPcf");
                add("35E2eKIEXXnP5q9L51iOAk");
            }})
            .spotifyURI("spotify:user:millersinc:playlist:6NEs2Ky3g64uhhpcjJWZPv")
            .build();

    private final RefinedPlaylist datasourceTestPlaylist2 = RefinedPlaylist.builder()
            .id("459xU1FWolimrAWkUpFEM9")
            .externalURL(new URL("https://open.spotify.com/user/millersinc/playlist/459xU1FWolimrAWkUpFEM9"))
            .numOfFollowers(3)
            .href(new URL("https://api.spotify.com/v1/users/millersinc/playlists/459xU1FWolimrAWkUpFEM9"))
            .imageURL(new URL("https://i.scdn.co/image/a4e52d20c83c723f8c40a215e5feb45c4db75cbb"))
            .name("Datasource Test Playlist 3")
            .description("Test 3")
            .userId("millersinc")
            .trackIds(new ArrayList<String>() {{
                add("6923H6OtBkxdFrepzaVXMd");
                add("4ZzFtk0U3BQalY3nLEaZJ9");
                add("5dKyZWlgjWw1oJgLa4GCZD");
            }})
            .spotifyURI("spotify:user:millersinc:playlist:459xU1FWolimrAWkUpFEM9")
            .build();

    private final RefinedPlaylist datasourceTestPlaylist3 = RefinedPlaylist.builder()
            .id("007EOmHh4iH9rk6hvVWGXt")
            .externalURL(new URL("https://open.spotify.com/user/millersinc/playlist/007EOmHh4iH9rk6hvVWGXt"))
            .numOfFollowers(2)
            .href(new URL("https://api.spotify.com/v1/users/millersinc/playlists/007EOmHh4iH9rk6hvVWGXt"))
            .imageURL(new URL("https://i.scdn.co/image/aff27e3ce2b5047d009405c292c64b3ccf9d629f"))
            .name("Datasource Test Playlist 3")
            .description("Test 3")
            .userId("notmillersinc")
            .trackIds(new ArrayList<String>() {{
                add("20CNpCKq1oTdvekXaboyeq");
                add("2374M0fQpWi3dLnB54qaLX");
                add("40dJCw4xU6Bd5ie9rfagNo");
            }})
            .spotifyURI("spotify:user:millersinc:playlist:007EOmHh4iH9rk6hvVWGXt")
            .build();


    public DaoFilteringIntegrationTest() throws MalformedURLException {
    }


    @Before
    public void beforeEach() throws Exception {
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(hostname, port, Network.localhostIsIPv6()))
                .build();

        mongodExe = starter.prepare(mongodConfig);
        mongod = mongodExe.start();
        refinedAlbumDao = new RefinedAlbumDao(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedAlbums");
        refinedArtistDao = new RefinedArtistDao(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedArtists");
        refinedPlaylistDao = new RefinedPlaylistDao(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedPlaylists");
        refinedTrackDao = new RefinedTrackDao(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedTracks");
        refinedUserDao = new RefinedUserDao(new ClientHandler(hostname, port, DATABASE_NAME), "RefinedUsers");
    }

    @After
    public void afterEach() {
        if (this.mongod != null) {
            this.mongod.stop();
            this.mongodExe.stop();
        }
    }

    @Test
    public void shouldGetAlbumsFromArtistId() {


        List<RefinedAlbum> albumsToPersist = new ArrayList<RefinedAlbum>() {{
            add(loveIsDead);
            add(everyOpenEye);
            add(raw);
        }};
        refinedAlbumDao.saveList(albumsToPersist);

        assertThat(refinedAlbumDao.getMongoCollection().count(), is(3L));

        List<RefinedAlbum> filteredForArtistChvrches = refinedAlbumDao.geAllfromArtistId(0, 10, "3CjlHNtplJyTf9npxaPl5w");

        assertThat(filteredForArtistChvrches.size(), is(2));
        assertThat(filteredForArtistChvrches.get(0), is(loveIsDead));
        assertThat(filteredForArtistChvrches.get(1), is(everyOpenEye));

    }

    @Test
    public void shouldSortByNewest() {

        List<RefinedAlbum> albumsToPersist = new ArrayList<RefinedAlbum>() {{
            add(loveIsDead);
            add(everyOpenEye);
            add(raw);
        }};
        refinedAlbumDao.saveList(albumsToPersist);

        assertThat(refinedAlbumDao.getMongoCollection().count(), is(3L));

        List<RefinedAlbum> sortedByNewest = refinedAlbumDao.getByReleaseDate(0, 10, SortOrder.Ascending);

        assertThat(sortedByNewest.get(0), is(loveIsDead));
        assertThat(sortedByNewest.get(1), is(raw));
        assertThat(sortedByNewest.get(2), is(everyOpenEye));
    }

    @Test
    public void shouldGetAlbumFromDate() {

        List<RefinedAlbum> albumsToPersist = new ArrayList<RefinedAlbum>() {{
            add(loveIsDead);
            add(everyOpenEye);
        }};
        refinedAlbumDao.saveList(albumsToPersist);

        assertThat(refinedAlbumDao.getMongoCollection().count(), is(2L));

        List<RefinedAlbum> datedList = refinedAlbumDao.getAlbumsFromDate(0, 10, new Date(1469746800000L));

        assertThat(datedList.get(0), is(everyOpenEye));
    }

    @Test
    public void shouldGetArtistsByGenre() {
        List<RefinedArtist> artistsToSave = new ArrayList<RefinedArtist>() {{
            add(nothingButThieves);
            add(theAmazons);
            add(walkTheMoon);
        }};
        refinedArtistDao.saveList(artistsToSave);

        assertThat(refinedArtistDao.getMongoCollection().count(), is(3L));

        List<RefinedArtist> genreList = refinedArtistDao.getAllByGenre(0, 10, "modern alternative rock");

        assertThat(genreList.size(), is(2));
        assertThat(genreList.get(0), is(nothingButThieves));
        assertThat(genreList.get(1), is(theAmazons));
    }

    @Test
    public void shouldGetMostFollowedArtists() {
        List<RefinedArtist> artistsToSave = new ArrayList<RefinedArtist>() {{
            add(nothingButThieves);
            add(theAmazons);
            add(walkTheMoon);
        }};
        refinedArtistDao.saveList(artistsToSave);

        assertThat(refinedArtistDao.getMongoCollection().count(), is(3L));

        List<RefinedArtist> artistsSortedByFollowed = refinedArtistDao.getMostFollowedArtists(0, 10);

        assertThat(artistsSortedByFollowed.size(), is(3));
        assertThat(artistsSortedByFollowed.get(0), is(walkTheMoon));
        assertThat(artistsSortedByFollowed.get(1), is(nothingButThieves));
        assertThat(artistsSortedByFollowed.get(2), is(theAmazons));

    }

    @Test
    public void shouldGetMostPopularPlaylists() {
        List<RefinedPlaylist> playlistsToSave = new ArrayList<RefinedPlaylist>() {{
            add(datasourceTestPlaylist1);
            add(datasourceTestPlaylist2);
            add(datasourceTestPlaylist3);
        }};
        refinedPlaylistDao.saveList(playlistsToSave);

        assertThat(refinedPlaylistDao.getMongoCollection().count(), is(3L));

        List<RefinedPlaylist> playlistSortedByFollowers = refinedPlaylistDao.getMostPopular(0, 10);

        assertThat(playlistSortedByFollowers.size(), is(3));
        assertThat(playlistSortedByFollowers.get(0), is(datasourceTestPlaylist2));
        assertThat(playlistSortedByFollowers.get(1), is(datasourceTestPlaylist3));
        assertThat(playlistSortedByFollowers.get(2), is(datasourceTestPlaylist1));
    }

    @Test
    public void shouldGetUsersPlaylists() {
        List<RefinedPlaylist> playlistsToSave = new ArrayList<RefinedPlaylist>() {{
            add(datasourceTestPlaylist1);
            add(datasourceTestPlaylist2);
            add(datasourceTestPlaylist3);
        }};
        refinedPlaylistDao.saveList(playlistsToSave);

        assertThat(refinedPlaylistDao.getMongoCollection().count(), is(3L));

        List<RefinedPlaylist> millersincPlaylists = refinedPlaylistDao.getByUserId("millersinc", 0, 10);

        assertThat(millersincPlaylists.size(), is(2));
        assertThat(millersincPlaylists.get(0), is(datasourceTestPlaylist2));
        assertThat(millersincPlaylists.get(1), is(datasourceTestPlaylist1));
    }

    @Test
    public void shouldGetPlaylistsContainingTrack() {
        List<RefinedPlaylist> playlistsToSave = new ArrayList<RefinedPlaylist>() {{
            add(datasourceTestPlaylist1);
            add(datasourceTestPlaylist2);
            add(datasourceTestPlaylist3);
        }};
        refinedPlaylistDao.saveList(playlistsToSave);

        assertThat(refinedPlaylistDao.getMongoCollection().count(), is(3L));

        List<RefinedPlaylist> playlistsContainingCertainTrack = refinedPlaylistDao.getPlaylistsContainingTrack("20CNpCKq1oTdvekXaboyeq", 0, 10);

        assertThat(playlistsContainingCertainTrack.size(), is(2));
        assertThat(playlistsContainingCertainTrack.get(0), is(datasourceTestPlaylist1));
        assertThat(playlistsContainingCertainTrack.get(1), is(datasourceTestPlaylist3));
    }
}
