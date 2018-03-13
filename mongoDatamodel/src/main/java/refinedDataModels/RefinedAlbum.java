package refinedDataModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefinedAlbum {
    private String id;
    private String name;
    private List<String> artistsIds;
    private List<String> trackIds;
    private URL imageURL;
    private Date releaseDate;
    private int popularity;
    private URL externalURL;
    private URL href;
    private URI uri;
}
