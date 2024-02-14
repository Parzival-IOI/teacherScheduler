package com.teacherScheduler.Scheduler.model;
import com.teacherScheduler.Scheduler.respository.ScheduleRepository;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(value = "Schedule")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Schedule {
    @Id
    private String id;
    @Field(name="Name")
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
