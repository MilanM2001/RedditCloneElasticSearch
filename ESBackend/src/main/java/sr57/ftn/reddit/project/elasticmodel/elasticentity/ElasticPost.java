package sr57.ftn.reddit.project.elasticmodel.elasticentity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "posts")
@Setting(settingPath = "/analyzers/serbianAnalyzer.json")
public class ElasticPost {
    @Id
    private Integer post_id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String text;

    @Field(type = FieldType.Integer)
    private Integer karma;

    @Field(type = FieldType.Integer)
    private Integer numberOfComments;

    @Field(type = FieldType.Object)
    private ElasticUser user;

    @Field(type = FieldType.Object)
    private ElasticCommunity community;

    @Field(type = FieldType.Object)
    private ElasticFlair flair;

    @Field(type = FieldType.Text)
    private String pdfDescription;

    private String filename;

    @Field(type = FieldType.Nested)
    private Set<ElasticComment> comments;
}
