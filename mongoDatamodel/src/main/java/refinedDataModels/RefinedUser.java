package refinedDataModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefinedUser {
    private String id;
    private String displayName;
    private URL externalURL;
    private Integer numOfFollowers;
    private URL href;
    private URL imageURL;
    private String spotifyURI;
}
