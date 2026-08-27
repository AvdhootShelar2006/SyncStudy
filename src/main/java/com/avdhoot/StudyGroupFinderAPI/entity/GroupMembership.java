package com.avdhoot.StudyGroupFinderAPI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"study_group_id", "member_id"} // unique combination
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class GroupMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "study_group_id")
    private StudyGroup group;
    private LocalDate joinedAt;
//    role

}
