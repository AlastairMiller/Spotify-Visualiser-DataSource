package refinedDataModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.javatic.mongo.jacksonCodec.objectId.Id;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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