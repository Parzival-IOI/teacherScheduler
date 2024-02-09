package com.teacherScheduler.Scheduler.services;

import com.teacherScheduler.Scheduler.dto.ScheduleRequest;
import com.teacherScheduler.Scheduler.dto.ScheduleResponse;
import com.teacherScheduler.Scheduler.model.Schedule;
import com.teacherScheduler.Scheduler.respository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    public void createSchedule(ScheduleRequest scheduleRequest) {

        List<String> e = new ArrayList<>();
        e.add("Hi");



        Schedule schedule = new Schedule(1, "5", "E1", "e", "2022-01-02", "2022-04-02", e);
        scheduleRepository.save(schedule);
        log.info("Saved {}", schedule.getId());
    }

    public List<ScheduleResponse> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream().map(this::mapToScheduleResponse).toList();
    }

    public ScheduleResponse mapToScheduleResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .Id(schedule.getId())
                .class_name(schedule.getClass_name())
                .department(schedule.getDepartment())
                .generation(schedule.getGeneration())
                .end_date(schedule.getEnd_date())
                .start_date(schedule.getStart_date())
                .schedule(schedule.getSchedule())
                .build();
    }
}
