package com.avdhoot.StudyGroupFinderAPI.repository.queryRepository;

import com.avdhoot.StudyGroupFinderAPI.entity.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupQueryRepository extends JpaRepository<Query, Integer> {
    Page<Query> findByGroup_Id(Pageable pageable, Integer id);
}
