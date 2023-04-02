package sr57.ftn.reddit.project.model.elasticmodel;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Setter
@Getter
@Builder
@AllArgsConstructor
@Document(indexName = "community")
public class CommunityElastic {

    @Id
    private Integer community_id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;
}
