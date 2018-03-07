package refinedDataModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefinedUser implements Serializable {
    private String id;
    private URI uri;
    private String displayName;
    private URL externalUrl;
    private Integer numOfFollowers;
    private String href;
    private List<String> imageUrls;
}
