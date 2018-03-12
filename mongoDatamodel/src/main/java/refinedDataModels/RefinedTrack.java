package refinedDataModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;

import java.net.URI;
import java.net.URL;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefinedTrack extends Document{
    private String id;
    private List<String> refinedAlbumIds;
    private List<String> refinedArtistIds;
    private int discNum;
    private int durationMs;
    private boolean explicit;
    private URL externalURL;
    private String href;
    private String name;
    private URL previewURL;
    private int trackNumber;
    private int popularity;
    private URI uri;
}