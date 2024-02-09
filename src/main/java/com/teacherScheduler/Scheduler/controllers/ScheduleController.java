package com.teacherScheduler.Scheduler.controllers;
import com.teacherScheduler.Scheduler.dto.ScheduleRequest;
import com.teacherScheduler.Scheduler.dto.ScheduleResponse;
import com.teacherScheduler.Scheduler.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void CreateSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        scheduleService.createSchedule(scheduleRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getSchedules() {
        return scheduleService.getAllSchedules();
    }
}
