package com.avdhoot.StudyGroupFinderAPI.repository.queryRepository;

import com.avdhoot.StudyGroupFinderAPI.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Reply, Integer> {

//  Page<Reply> findByGroup_IdAndReply_Id(Pageable pageable, int groupId, int replyId);


  // Fetches all the replies for the given queryId
  Page<Reply> findByGroup_IdAndQuery_QueryId(int groupId, int queryId, Pageable pageable);


//  @Query(
//          "SELECT answer FROM Reply answer WHERE answer.studyGroup.id = :groupId AND answer.query.id = :queryId"
//  )
//  Page<Reply> findSolutions(Pageable pageable, @Param("groupId") int groupId, @Param("queryId") int queryId);
}
