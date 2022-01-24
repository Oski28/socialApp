package com.example.socialApp.report.converter;

import com.example.socialApp.report.dto.ReportShowDto;
import com.example.socialApp.report.model_repo.Report;
import com.example.socialApp.shared.BaseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReportShowConverter extends BaseConverter<Report, ReportShowDto> {


    @Override
    public Function<ReportShowDto, Report> toEntity() {
        return null;
    }

    @Override
    public Function<Report, ReportShowDto> toDto() {
        return report -> {
            if (report == null)
                return null;

            ReportShowDto dto = new ReportShowDto();

            dto.setUserId(report.getUser().getId());
            if (report.getEvent() != null) {
                dto.setEventId(report.getEvent().getId());
            }
            dto.setAddDate(report.getAddDate());
            dto.setReason(report.getReason());
            dto.setReceived(report.getReceived());
            dto.setId(report.getId());

            return dto;
        };
    }
}
