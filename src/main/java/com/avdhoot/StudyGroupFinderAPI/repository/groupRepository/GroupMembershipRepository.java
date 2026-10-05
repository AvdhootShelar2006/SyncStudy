package com.avdhoot.StudyGroupFinderAPI.repository.groupRepository;

import com.avdhoot.StudyGroupFinderAPI.entity.GroupMembership;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import com.avdhoot.StudyGroupFinderAPI.enums.GroupRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Integer> {

    boolean existsByGroupAndUser(Group group, User user);

    Page<GroupMembership> findByGroup_Id(Pageable pageable, int groupId);
    
    Optional<GroupMembership> findByGroup_IdAndUser_Id(Integer id, Integer id1);

    boolean existsByGroup_IdAndUser_IdAndGroupRole(Integer groupId, Integer userId, GroupRole groupRole);

    boolean existsByGroup_IdAndUser_Id(Integer groupId, Integer userId);

    @Query("""
    SELECT gm.group
    FROM GroupMembership gm
    WHERE gm.user.id = :userId
""")
    Page<Group> findGroupsByUserId(
            @Param("userId") Integer userId,
            Pageable pageable
    );
}
