package com.example.socialApp.report.web;

import com.example.socialApp.event.model_repo.Event;
import com.example.socialApp.report.model_repo.Report;
import com.example.socialApp.report.model_repo.ReportRepository;
import com.example.socialApp.shared.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImplementation implements BaseService<Report>, ReportService {

    private final ReportRepository reportRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Report> getAll(int page, int size, String column, Sort.Direction direction) {
        Sort sort = Sort.by(new Sort.Order(direction, column));
        return this.reportRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean update(Long id, Report entity) {
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public boolean delete(Long id) {
        Optional<Report> reportOptional = findById(id);
        if (reportOptional.isPresent()) {
            this.reportRepository.delete(reportOptional.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Report save(Report entity) {
        return this.reportRepository.save(entity);
    }

    @Override
    public Optional<Report> findById(Long id) {
        return this.reportRepository.findById(id);
    }

    @Override
    public Report getById(Long id) {
        return this.reportRepository.getById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void deleteAllForDeletedEvent(Event event) {
        List<Report> reports = this.reportRepository.findAll().stream()
                .filter(report -> {
                    if (report.getEvent() != null)
                        return report.getEvent().getId().equals(event.getId());
                    else return false;
                }).collect(Collectors.toList());
        for (Report report : reports) {
            this.reportRepository.delete(report);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void received(Long id) {
        Report report = getById(id);
        report.setReceived(true);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Report> getAllNotReceived(int page, int size, String column, Sort.Direction sortDir) {
        Sort sort = Sort.by(new Sort.Order(sortDir, column));
        return this.reportRepository.findAllByReceivedFalse(PageRequest.of(page, size, sort));
    }
}
