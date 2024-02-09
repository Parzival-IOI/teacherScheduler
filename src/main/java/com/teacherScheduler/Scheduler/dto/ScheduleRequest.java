package com.teacherScheduler.Scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ScheduleRequest {
    private String className;
    private String generation;
    private String department;
    private String start_date;
    private String end_date;
    private List<Integer> teacherId;
    private Integer studentNumber;
}