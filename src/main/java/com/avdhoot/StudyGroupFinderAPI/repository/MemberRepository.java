package com.avdhoot.StudyGroupFinderAPI.repository;

import com.avdhoot.StudyGroupFinderAPI.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Integer> {
    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Please provide a valid email address") String email);
}
