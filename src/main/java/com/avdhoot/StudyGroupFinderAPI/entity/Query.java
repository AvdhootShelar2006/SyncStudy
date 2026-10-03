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
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer queryId;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "postedBy_id")
    private User postedBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    private boolean isResolved;
    private LocalDate createdAt;
}
