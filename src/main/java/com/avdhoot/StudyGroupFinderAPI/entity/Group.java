package com.avdhoot.StudyGroupFinderAPI.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "community_group_table")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String groupName;
    private String subject;
    private String field;
    private String description;
    private Integer maxMembers;
    private String tags;
    private Boolean isOpen;
    private Integer totalMembers;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createdAt;

    @Column(name = "group_admin")
    private String createdBy;
    private Boolean isEnable;

    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<GroupMembership> memberships;

    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Query> queries;

    @OneToMany(
            mappedBy = "targetGroup",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Report> reports;
}
