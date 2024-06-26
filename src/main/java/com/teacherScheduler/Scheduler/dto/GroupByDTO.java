package com.teacherScheduler.Scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GroupByDTO {
    private String name;
    private String generation;
    private String department;
}
