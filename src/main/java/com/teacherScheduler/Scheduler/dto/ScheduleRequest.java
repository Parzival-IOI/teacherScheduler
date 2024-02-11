package com.teacherScheduler.Scheduler.dto;

import com.teacherScheduler.Scheduler.Generator.Course;
import com.teacherScheduler.Scheduler.Generator.Teacher;
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
    private String generation;
    private String department;
    private List<Course> courses;
    private List<Teacher> teachers;
}
