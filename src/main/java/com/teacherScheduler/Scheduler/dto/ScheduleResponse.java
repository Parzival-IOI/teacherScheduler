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
    private String class_name;
    private String generation;
    private String department;
    private String part_of_day;
    private String day;
    private Integer period;
    private String course;
    private String teacher_id;
    private String teacher_name;
}
