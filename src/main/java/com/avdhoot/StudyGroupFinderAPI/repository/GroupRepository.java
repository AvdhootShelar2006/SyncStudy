package com.avdhoot.StudyGroupFinderAPI.repository;

import com.avdhoot.StudyGroupFinderAPI.model.entities.StudyGroup;
import com.avdhoot.StudyGroupFinderAPI.model.interaction.GroupQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<StudyGroup, Integer> {
    Optional<GroupQuery> findById(StudyGroup group);
}
