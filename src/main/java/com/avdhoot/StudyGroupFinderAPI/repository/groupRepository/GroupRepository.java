package com.avdhoot.StudyGroupFinderAPI.repository.groupRepository;

import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.model.interaction.GroupQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<StudyGroup, Integer> {
    Optional<GroupQuery> findById(StudyGroup group);

    @Query("select g from StudyGroup g where " +
            "lower(g.name) like lower(concat('%', :keyword, '%')) or " +
            "lower(g.subject) like lower(concat('%', :keyword, '%')) or " +
            "lower(g.field) like lower(concat('%', :keyword, '%')) or " +
            "lower(g.description) like lower(concat('%', :keyword, '%')) or " +
            "lower(g.tags) like lower(concat('%', :keyword, '%'))")
    List<StudyGroup> searchUsingKeyword(@Param("keyword") String keyword);
}
