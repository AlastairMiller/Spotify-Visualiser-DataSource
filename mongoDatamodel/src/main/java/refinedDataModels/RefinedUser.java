package refinedDataModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefinedUser {
    private String id;
    private String displayName;
    private URL externalURL;
    private Integer numOfFollowers;
    private URL href;
    private URL imageURL;
    private String spotifyURI;
}
