package com.avdhoot.StudyGroupFinderAPI.model.interaction;

import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AnswerQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;

    // to which query this answer belongs
    @ManyToOne
    @JoinColumn(name = "query_id")
    private GroupQuery groupQuery;


    // to which group this answer query belongs
    @ManyToOne
    @JoinColumn(name = "group_id")
    private StudyGroup studyGroup;
    private LocalDate createdAt;
}


/*
* 5. Answer
   → id
   → content
   → answeredBy (student)
   → query (which GroupQuery)
   → createdAt
   * */