package com.avdhoot.StudyGroupFinderAPI.repository.groupRepository;

import com.avdhoot.StudyGroupFinderAPI.entity.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.entity.Member;
import com.avdhoot.StudyGroupFinderAPI.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Integer> {
    List<GroupMembership> findByGroup_Id(int groupId);

    Optional<GroupMembership> findByGroup_IdAndMember_Id(int groupId, int memberId);

    boolean existsByGroup_IdAndMember_Id(int groupId, int i);

    boolean existsByGroupAndMember(StudyGroup group, Member member);

}
