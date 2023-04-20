package sr57.ftn.reddit.project.elasticmodel.elasticentity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "communities")
@Setting(settingPath = "/analyzers/serbianAnalyzer.json")
public class ElasticCommunity {
    @Id
    private Integer community_id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Integer)
    private Integer numberOfPosts;

    @Field(type = FieldType.Double)
    private Double averageKarma;

    @Field(type = FieldType.Text)
    private String pdfDescription;

    private String filename;
}
