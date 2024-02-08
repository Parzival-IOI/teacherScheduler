package com.teacherScheduler.Scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

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
    private Date start_date;
    private Date end_date;
    private List<Integer> teacherId;
    private List<Integer> studentNumber;
}
