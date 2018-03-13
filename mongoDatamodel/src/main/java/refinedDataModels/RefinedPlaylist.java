package refinedDataModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URL;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefinedPlaylist {
    private String id;
    private URI uri;
    private URL externalURL;
    private Integer numOfFollowers;
    private URL href;
    private List<String> imageURLs;
    private String name;
    private String refinedUserId;
    private List<String> refinedTrackIds;
}
