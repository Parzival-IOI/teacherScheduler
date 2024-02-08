package com.teacherScheduler.Scheduler.controllers;
import com.teacherScheduler.Scheduler.dto.ScheduleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void CreateSchedule(@RequestBody ScheduleRequest scheduleRequest) {

    }
}
