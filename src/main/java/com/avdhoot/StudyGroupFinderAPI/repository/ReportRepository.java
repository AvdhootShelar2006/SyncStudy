package com.avdhoot.StudyGroupFinderAPI.repository;

import com.avdhoot.StudyGroupFinderAPI.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    Page<Report> findByTargetGroupId_Id(int groupId, Pageable pageable);

    Optional<Report> findByTargetGroupId_IdAndId(
            int groupId,
            int reportId
    );
}
