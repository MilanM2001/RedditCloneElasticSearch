package sr57.ftn.reddit.project.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;

import java.io.Serializable;

public enum ReportReason implements Serializable {
    @JsonProperty("Breaks Rules")
    BREAKS_RULES("Breaks Rules"),

    @JsonProperty("Harassment")
    HARASSMENT("Harassment"),

    @JsonProperty("Hate")
    HATE("Hate"),

    @JsonProperty("Sharing Personal Information")
    SHARING_PERSONAL_INFORMATION("Sharing Personal Information"),

    @JsonProperty("Impersonation")
    IMPERSONATION("Impersonation"),

    @JsonProperty("Copyright Violation")
    COPYRIGHT_VIOLATION("Copyright Violation"),

    @JsonProperty("Trademark Violation")
    TRADEMARK_VIOLATION("Trademark Violation"),

    @JsonProperty("Spam")
    SPAM("Spam"),

    @JsonProperty("Self Harm or Suicide")
    SELF_HARM_OR_SUICIDE("Self Harm or Suicide"),

    @JsonProperty("Other")
    OTHER("Other");

    private final String friendlyName;

    ReportReason(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @NonNull
    @Override
    public String toString() {
        return friendlyName;
    }
//    @JsonProperty("Breaks Rules")
//    BREAKS_RULES("Breaks Rules"),
//
//    @JsonProperty("Harassment")
//    HARASSMENT("Harassment"),
//
//    @JsonProperty("Hate")
//    HATE("Hate"),
//
//    @JsonProperty("Sharing Personal Information")
//    SHARING_PERSONAL_INFORMATION("Sharing Personal Information"),
//
//    @JsonProperty("Impersonation")
//    IMPERSONATION("Impersonation"),
//
//    @JsonProperty("Copyright Violation")
//    COPYRIGHT_VIOLATION("Copyright Violation"),
//
//    @JsonProperty("Trademark Violation")
//    TRADEMARK_VIOLATION("Trademark Violation"),
//
//    @JsonProperty("Spam")
//    SPAM("Spam"),
//
//    @JsonProperty("Self Harm or Suicide")
//    SELF_HARM_OR_SUICIDE("Self Harm or Suicide"),
//
//    @JsonProperty("Other")
//    OTHER("Other");
//
//    private final String friendlyName;
//
//    ReportReason(String friendlyName){
//        this.friendlyName = friendlyName;
//    }
//
//    @Override public String toString(){
//        return friendlyName;
//    }
}
