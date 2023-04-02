package sr57.ftn.reddit.project.model.dto.reportDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sr57.ftn.reddit.project.model.enums.ReportReason;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddReportDTO implements Serializable {
    private ReportReason report_reason;
}
