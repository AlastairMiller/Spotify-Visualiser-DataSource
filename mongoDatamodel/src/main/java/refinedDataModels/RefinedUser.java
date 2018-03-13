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
public class RefinedUser {
    private String id;
    private URI uri;
    private String displayName;
    private URL externalUrl;
    private Integer numOfFollowers;
    private URL href;
    private List<String> imageUrls;
}
