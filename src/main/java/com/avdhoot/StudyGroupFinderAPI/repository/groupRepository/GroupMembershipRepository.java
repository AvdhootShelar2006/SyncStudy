package com.avdhoot.StudyGroupFinderAPI.repository.groupRepository;

import com.avdhoot.StudyGroupFinderAPI.entity.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Integer> {
  


    boolean existsByGroupAndUser(Group group, User user);

    Page<GroupMembership> findByGroup_Id(Pageable pageable, int groupId);

    boolean existsByGroup_IdAndUser_Id(Integer id, Integer id1);

    Optional<GroupMembership> findByGroup_IdAndUser_Id(Integer id, Integer id1);
}
