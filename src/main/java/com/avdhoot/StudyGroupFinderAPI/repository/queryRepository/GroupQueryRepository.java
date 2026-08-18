package com.avdhoot.StudyGroupFinderAPI.repository.queryRepository;

import com.avdhoot.StudyGroupFinderAPI.model.interaction.GroupQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GroupQueryRepository extends JpaRepository<GroupQuery, Integer> {
    List<GroupQuery> findByStudyGroup_Id(int groupId);
}
