package com.avdhoot.StudyGroupFinderAPI.model;

import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.model.entities.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Reported_By")
    private Member reportedBy;

    @ManyToOne
    @JoinColumn(name="traget_Group_Id")
    private StudyGroup targetGroupId;

    @ManyToOne
    @JoinColumn(name = "target_member_id")
    private Member targetMemberId;
    private String reason;
    private ReportStatus status;
    private LocalDateTime createdAt;
}
