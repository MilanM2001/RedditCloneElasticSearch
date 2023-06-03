package sr57.ftn.reddit.project.elasticmodel.elasticentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ElasticRule {
    @Field(type = FieldType.Text)
    private String description;
}
