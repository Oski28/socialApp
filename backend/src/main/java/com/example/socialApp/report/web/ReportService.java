package com.example.socialApp.report.web;

import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.report.model_repo.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;


public interface ReportService {

    void deleteAllForDeletedEvent(Event event);

    void received(Long id);

    Page<Report> getAllNotReceived(int page, int size, String column, Sort.Direction sortDir);
}
