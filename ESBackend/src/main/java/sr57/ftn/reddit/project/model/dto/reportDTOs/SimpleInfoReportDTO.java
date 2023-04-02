package sr57.ftn.reddit.project.model.dto.reportDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sr57.ftn.reddit.project.model.dto.commentDTOs.SimpleInfoCommentDTO;
import sr57.ftn.reddit.project.model.dto.postDTOs.SimpleInfoPostDTO;
import sr57.ftn.reddit.project.model.dto.userDTOs.SimpleInfoUserDTO;
import sr57.ftn.reddit.project.model.enums.ReportReason;
import sr57.ftn.reddit.project.model.enums.ReportStatus;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleInfoReportDTO implements Serializable {
    private Integer report_id;
    private ReportReason report_reason;
    private ReportStatus report_status;
    private Boolean accepted;
    private SimpleInfoUserDTO user;
    private SimpleInfoPostDTO post;
    private SimpleInfoCommentDTO comment;
}
