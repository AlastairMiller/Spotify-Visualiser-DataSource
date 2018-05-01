package refinedDataModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefinedPlaylist {
    private String id;
    private URL externalURL;
    private Integer numOfFollowers;
    private URL href;
    private URL imageURL;
    private String name;
    private String description;
    private String userId;
    private List<String> trackIds;
    private String spotifyURI;
}
