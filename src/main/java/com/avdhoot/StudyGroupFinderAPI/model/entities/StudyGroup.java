package com.avdhoot.StudyGroupFinderAPI.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String subject;
    private String field;
    private String description;
    private Integer maxMembers;

    private String tags;
    private Boolean isOpen;
    private LocalDate createdAt;
    private String createdBy;

    public StudyGroup(int i) {
    }
}
