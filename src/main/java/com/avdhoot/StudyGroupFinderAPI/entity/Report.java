package com.avdhoot.StudyGroupFinderAPI.entity;

import com.avdhoot.StudyGroupFinderAPI.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Reported_By")
    private Member reportedBy;

    @ManyToOne
    @JoinColumn(name="target_Group_Id")
    private StudyGroup targetGroupId;

    @ManyToOne
    @JoinColumn(name = "target_member_id")
    private Member targetMember;
    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;
    private LocalDateTime reportedTime;
}
