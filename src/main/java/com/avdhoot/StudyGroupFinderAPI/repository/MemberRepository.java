package com.avdhoot.StudyGroupFinderAPI.repository;

import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Integer> {
}
