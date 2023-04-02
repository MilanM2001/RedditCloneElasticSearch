package sr57.ftn.reddit.project.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;

import java.io.Serializable;

public enum ReportStatus implements Serializable {
    @JsonProperty("Waiting")
    WAITING("Waiting"),

    @JsonProperty("Accepted")
    ACCEPTED("Accepted"),

    @JsonProperty("Denied")
    DENIED("Denied");

    private final String friendlyName;

    ReportStatus(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @NonNull
    @Override
    public String toString() {
        return friendlyName;
    }
}
