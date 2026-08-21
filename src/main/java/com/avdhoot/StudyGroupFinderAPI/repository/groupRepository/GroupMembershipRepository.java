package com.avdhoot.StudyGroupFinderAPI.repository.groupRepository;

import com.avdhoot.StudyGroupFinderAPI.model.entities.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Integer> {
    List<GroupMembership> findByGroup_Id(int groupId);

    Optional<GroupMembership> findByGroup_IdAndMember_Id(int groupId, int memberId);

    List<GroupMembership> findByGroup_IdAndJoinedAtAfter(int groupId, LocalDate startDate, Pageable pageable);

    List<GroupMembership> findByGroup_IdAndJoinedAtBetween(int groupId, LocalDate startDate, Optional<LocalDate> endDate, Pageable pageable);

    boolean existsByGroup_IdAndMember_Id(int groupId, int i);

    boolean existsByGroupAndMember(StudyGroup group, Member member);

}
