package refinedDataModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.javatic.mongo.jacksonCodec.objectId.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefinedAlbum {
    private String id;
    private String name;
    private List<String> artistsIds;
    private List<String> trackIds;
    private URL imageURL;
    private Date releaseDate;
    private Integer popularity;
    private URL externalURL;
    private URL href;
    private String spotifyURI;
}
