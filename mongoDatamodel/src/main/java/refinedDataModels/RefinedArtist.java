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
public class RefinedArtist {
    private String id;
    private URL externalURL;
    private List<String> genres;
    private List<String> refinedAlbumIds;
    private URL href;
    private String name;
    private int followers;
    private int popularity;
    private URI uri;
}
