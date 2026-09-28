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
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer replyId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // to which query this answer belongs
    @ManyToOne
    @JoinColumn(name = "query_id")
    private Query query;


    // to which group this answer query belongs
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    private LocalDate createdAt;
}


/*
* 5. Answer
   → id
   → content
   → answeredBy (student)
   → query (which Query)
   → createdAt
   * */