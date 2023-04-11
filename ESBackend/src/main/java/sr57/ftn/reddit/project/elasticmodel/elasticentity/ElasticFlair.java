package sr57.ftn.reddit.project.elasticmodel.elasticentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ElasticFlair {
    @Id
    private Integer flair_id;

    @Field(type = FieldType.Text)
    private String name;
}
