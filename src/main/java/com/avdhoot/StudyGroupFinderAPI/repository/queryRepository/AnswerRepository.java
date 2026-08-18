package com.avdhoot.StudyGroupFinderAPI.repository.queryRepository;

import com.avdhoot.StudyGroupFinderAPI.model.interaction.AnswerQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerQuery, Integer> {
  @Query(
          "SELECT answer FROM AnswerQuery answer WHERE answer.studyGroup.id = :groupId AND answer.groupQuery.id = :queryId"
  )
    List<AnswerQuery> findSolutions(@Param("groupId") int groupId, @Param("queryId") int queryId);
}
