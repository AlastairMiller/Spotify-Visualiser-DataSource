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
