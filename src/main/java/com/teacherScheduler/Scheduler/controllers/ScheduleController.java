package com.teacherScheduler.Scheduler.controllers;
import com.teacherScheduler.Scheduler.dto.ScheduleRequest;
import com.teacherScheduler.Scheduler.dto.ScheduleResponse;
import com.teacherScheduler.Scheduler.model.Schedule;
import com.teacherScheduler.Scheduler.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        scheduleService.createSchedule(scheduleRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getSchedules() {
        return scheduleService.getAllSchedules();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public Optional<ScheduleResponse> getSchedule(@PathVariable String id) {
        return Optional.ofNullable(scheduleService.getSchedule(id));
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public String updateSchedule(@RequestBody Schedule schedule) {
        boolean isUpdate = scheduleService.UpdateSchedule(schedule);
        if(isUpdate) {
            return "Success";
        }
        return "Error";
    }

    @GetMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.GONE)
    public void deleteSchedule(@PathVariable String id) {
        scheduleService.deleteSchedule(id);
    }
}
