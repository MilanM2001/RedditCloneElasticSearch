package sr57.ftn.reddit.project.model.entity;

import lombok.*;
import sr57.ftn.reddit.project.model.enums.ReportReason;
import sr57.ftn.reddit.project.model.enums.ReportStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report")
public class Report implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", unique = true, nullable = false)
    private Integer report_id;

    @Column(name = "report_reason", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportReason report_reason;

    @Column(name = "report_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus report_status;

    @Column(name = "timestamp", nullable = false)
    private LocalDate timestamp;

    @Column(name = "accepted", nullable = false)
    private Boolean accepted;

    //Report has one Comment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;

    //Report has one Post
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    //Report has one User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
