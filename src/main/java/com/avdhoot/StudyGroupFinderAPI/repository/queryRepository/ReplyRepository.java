package com.avdhoot.StudyGroupFinderAPI.repository.queryRepository;

import com.avdhoot.StudyGroupFinderAPI.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {



  // Fetches all the replies for the given queryId
  Page<Reply> findByGroup_IdAndQuery_QueryId(int groupId, int queryId, Pageable pageable);

    boolean existsByUser_IdAndReplyId(Integer userId, Integer replyId);


  Optional<Reply> findByReplyIdAndQuery_QueryIdAndGroup_Id(
          Integer replyId,
          Integer queryId,
          Integer groupId
  );
}
