package com.example.socialApp.report.web;

import com.example.socialApp.report.converter.ReportConverter;
import com.example.socialApp.report.converter.ReportShowConverter;
import com.example.socialApp.report.dto.ReportDto;
import com.example.socialApp.report.dto.ReportShowDto;
import com.example.socialApp.report.model_repo.Report;
import com.example.socialApp.shared.BaseController;
import com.example.socialApp.shared.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/reports", produces = "application/json")
public class ReportController extends BaseController<Report> {

    private final ReportServiceImplementation reportService;

    private final ReportShowConverter reportShowConvert;

    private final ReportConverter reportConverter;


    @Autowired
    public ReportController(BaseService<Report> service, ReportServiceImplementation reportService,
                            ReportShowConverter reportShowConvert, ReportConverter reportConverter) {
        super(service);
        this.reportService = reportService;
        this.reportShowConvert = reportShowConvert;
        this.reportConverter = reportConverter;
    }

    /* GET */

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<ReportShowDto> getOne(@PathVariable final Long id) {
        this.reportService.received(id);
        return super.getOne(id, this.reportShowConvert.toDto());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Page<ReportShowDto>> getAll(@RequestParam(defaultValue = "0") final int page,
                                                      @RequestParam(defaultValue = "20") final int size,
                                                      @RequestParam(defaultValue = "id") final String column,
                                                      @RequestParam(defaultValue = "ASC") final String direction) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(this.reportService.getAll(page, size, column, sortDir)
                .map(this.reportShowConvert.toDto()));
    }

    @GetMapping("/notReceived")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Page<ReportShowDto>> getAllNotReceived(@RequestParam(defaultValue = "0") final int page,
                                                                 @RequestParam(defaultValue = "20") final int size,
                                                                 @RequestParam(defaultValue = "id") final String column,
                                                                 @RequestParam(defaultValue = "ASC") final String direction) {
        Sort.Direction sortDir = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(this.reportService.getAllNotReceived(page, size, column, sortDir)
                .map(this.reportShowConvert.toDto()));
    }

    /* POST */

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@RequestBody @Valid final ReportDto dto) {
        return super.create(this.reportConverter.toEntity().apply(dto));
    }

    /* DELETE */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        if (this.reportService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
