package com.example.socialApp.report.converter;

import com.example.socialApp.event.model_repo.EventRepository;
import com.example.socialApp.report.dto.ReportDto;
import com.example.socialApp.report.model_repo.Report;
import com.example.socialApp.shared.BaseConverter;
import com.example.socialApp.user.model_repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReportConverter extends BaseConverter<Report, ReportDto> {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Override
    public Function<ReportDto, Report> toEntity() {
        return dto -> {
            if (dto == null)
                return null;

            Report report = new Report();
            report.setReason(dto.getReason());
            report.setAddDate(LocalDateTime.now());
            report.setReceived(false);
            if (dto.getEventId() != null) {
                report.setEvent(eventRepository.getById(dto.getEventId()));
            }
            report.setUser(userRepository.getById(dto.getUserId()));

            return report;
        };
    }

    @Override
    public Function<Report, ReportDto> toDto() {
        return null;
    }
}
