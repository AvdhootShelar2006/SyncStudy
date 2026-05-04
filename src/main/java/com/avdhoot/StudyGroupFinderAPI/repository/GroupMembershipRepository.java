package com.avdhoot.StudyGroupFinderAPI.repository;

import com.avdhoot.StudyGroupFinderAPI.model.entities.GroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Integer> {
    List<GroupMembership> findByGroup_Id(int groupId);

    Optional<GroupMembership> findByGroup_IdAndMember_Id(int groupId, int memberId);
}
