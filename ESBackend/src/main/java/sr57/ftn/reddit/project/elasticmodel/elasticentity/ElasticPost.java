package sr57.ftn.reddit.project.elasticmodel.elasticentity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import sr57.ftn.reddit.project.model.entity.User;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "posts")
public class ElasticPost {
    @Id
    private Integer post_id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String text;

    @Field(type = FieldType.Object)
    private ElasticUser user;

    @Field(type = FieldType.Object)
    private ElasticCommunity community;

//    @Field(type = FieldType.Object)
//    private ElasticFlair flair;

}
