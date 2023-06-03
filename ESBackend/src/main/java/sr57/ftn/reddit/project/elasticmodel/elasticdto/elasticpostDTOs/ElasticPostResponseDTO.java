package sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs;

import lombok.Builder;
import lombok.Data;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticComment;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticFlair;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticUser;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class ElasticPostResponseDTO {
    private Integer post_id;
    private String title;
    private String text;
    private Integer karma;
    private Integer numberOfComments;
    private ElasticCommunity community;
    private ElasticUser user;
    private ElasticFlair flair;
    private String pdfDescription;
    private Set<ElasticComment> comments;
}
