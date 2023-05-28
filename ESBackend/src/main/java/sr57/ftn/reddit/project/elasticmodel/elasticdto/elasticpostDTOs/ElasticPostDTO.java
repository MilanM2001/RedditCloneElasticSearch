package sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ElasticPostDTO {
    private Integer post_id;
    private String title;
    private String text;
    private MultipartFile[] files;
    private Integer community_id;
    private Integer user_id;
    private Integer flair_id;
}
