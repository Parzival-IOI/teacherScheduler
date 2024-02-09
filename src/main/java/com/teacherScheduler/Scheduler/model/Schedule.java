package com.teacherScheduler.Scheduler.model;
import com.teacherScheduler.Scheduler.respository.ScheduleRepository;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document(value = "Schedule")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Schedule {
    @Id
    private Integer id;
    private String generation;
    @Field(name="Name")
    private String class_name;
    private String department;
    private String start_date;
    private String end_date;
    private List<String> schedule;
}
