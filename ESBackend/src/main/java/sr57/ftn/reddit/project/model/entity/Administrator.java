package sr57.ftn.reddit.project.model.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Administrator extends User {
    @Override
    public String getRole() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }
}
