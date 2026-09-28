package com.avdhoot.StudyGroupFinderAPI.repository.groupRepository;

import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import com.avdhoot.StudyGroupFinderAPI.entity.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    Optional<Query> findById(Group group);

    @org.springframework.data.jpa.repository.Query("select g from Group g where " +
            "lower(g.name) like lower(concat('%', :keyword, '%')) or " +
            "lower(g.subject) like lower(concat('%', :keyword, '%')) or " +
            "lower(g.field) like lower(concat('%', :keyword, '%')) or " +
            "lower(g.description) like lower(concat('%', :keyword, '%')) or " +
            "lower(g.tags) like lower(concat('%', :keyword, '%'))")
    List<Group> searchUsingKeyword(@Param("keyword") String keyword);

    boolean existsByName(String name);
}
