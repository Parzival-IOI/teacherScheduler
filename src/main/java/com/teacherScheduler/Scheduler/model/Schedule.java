package com.teacherScheduler.Scheduler.model;
import com.teacherScheduler.Scheduler.respository.ScheduleRepository;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(value = "Teacher")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Schedule {
    @Id
    private String Id;
    private String generation;
    private String class_name;
    private Date start_date;
    private Date end_date;
    private String department;
    private String schedule;
}
