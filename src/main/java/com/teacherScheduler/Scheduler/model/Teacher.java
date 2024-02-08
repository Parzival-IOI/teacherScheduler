package com.teacherScheduler.Scheduler.model;
import com.teacherScheduler.Scheduler.respository.ScheduleRepository;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Teacher {
    private String name;
    private String phone;
    private String address;
    private String email;
    private Date dob;
    private boolean active;
}
