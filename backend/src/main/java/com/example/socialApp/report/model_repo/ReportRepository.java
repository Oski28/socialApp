package com.example.socialApp.report.model_repo;

import com.example.socialApp.shared.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends BaseRepository<Report> {

    Page<Report> findAllByReceivedFalse(Pageable pageable);
}
