package com.teacherScheduler.Scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {
    private String id;
    private String name;
    private int isMorning;
    private int isAfternoon;
    private int isEvening;
    private List<String> teach;
}
