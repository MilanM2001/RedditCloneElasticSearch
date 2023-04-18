package sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ElasticCommunityDTO {
    private String name;
    private String description;
    private MultipartFile[] files;
}
