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
public class RefinedTrack {
    private String id;
    private String albumId;
    private List<String> artistIds;
    private Integer discNumber;
    private Integer durationMs;
    private Boolean explicit;
    private URL externalURL;
    private URL href;
    private String name;
    private URL previewURL;
    private Integer trackNumber;
    private Integer popularity;
    private String spotifyURI;
}