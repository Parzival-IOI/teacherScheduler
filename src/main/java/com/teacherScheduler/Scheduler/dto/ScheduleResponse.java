package com.teacherScheduler.Scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponse {
    private String id;
    private String generation;
    private String class_name;
    private String start_date;
    private String end_date;
    private String department;
    private List<String> schedule;
}
