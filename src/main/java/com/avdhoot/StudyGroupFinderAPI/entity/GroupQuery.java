package com.avdhoot.StudyGroupFinderAPI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer queryId;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member postedBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private StudyGroup studyGroup;

    private boolean isResolved;
    private LocalDate createdAt;
}
