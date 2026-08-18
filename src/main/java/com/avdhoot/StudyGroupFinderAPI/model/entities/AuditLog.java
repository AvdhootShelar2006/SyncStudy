package com.avdhoot.StudyGroupFinderAPI.model.entities;

import com.avdhoot.StudyGroupFinderAPI.model.entities.enums.ActionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private Integer group_Id;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private LocalDateTime timestamp;
}
