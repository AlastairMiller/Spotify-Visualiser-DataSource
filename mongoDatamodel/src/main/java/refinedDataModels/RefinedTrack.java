package refinedDataModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefinedTrack implements Serializable {
    private String id;
    private List<String> refinedArtistsIds;
    private List<String> refinedPlaylistsIds;
    private List<String> availableMarkets;
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