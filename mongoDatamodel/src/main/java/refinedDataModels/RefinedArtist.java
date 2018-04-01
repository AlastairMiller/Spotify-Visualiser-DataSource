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
public class RefinedArtist {
    private String id;
    private URL externalURL;
    private List<String> genres;
    private URL href;
    private String name;
    private Integer followers;
    private Integer popularity;
    private String spotifyURI;
}
