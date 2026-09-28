package com.avdhoot.StudyGroupFinderAPI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private User user;

    @ManyToOne
    @JoinColumn(name = "study_group_id")
    private Group group;
    private LocalDate joinedAt;
//    role
@ManyToMany
@JoinTable(
        name = "gorup_membership_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
)
private Set<Roles> roles = new HashSet<>();

}
