package com.teacherScheduler.Scheduler.services;

import com.teacherScheduler.Scheduler.Generator.Schedule_Generator;
import com.teacherScheduler.Scheduler.dto.ScheduleRequest;
import com.teacherScheduler.Scheduler.dto.ScheduleResponse;
import com.teacherScheduler.Scheduler.model.Schedule;
import com.teacherScheduler.Scheduler.respository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    public void createSchedule(ScheduleRequest scheduleRequest) {


        Schedule_Generator.generateSchedule(scheduleRequest.getCourses(), scheduleRequest.getTeachers(), scheduleRequest.getGeneration(), scheduleRequest.getDepartment());

    }

    public List<ScheduleResponse> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream().map(this::mapToScheduleResponse).toList();
    }

    public ScheduleResponse mapToScheduleResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .class_name(schedule.getClass_name())
                .department(schedule.getDepartment())
                .generation(schedule.getGeneration())
                .build();
    }

    public Optional<Schedule> getSchedule(String id) {
        return scheduleRepository.findById(id);
    }

    public boolean UpdateSchedule(Schedule schedule) {
        Optional<Schedule> foundSchedule = scheduleRepository.findById(schedule.getId());
        if(foundSchedule.isPresent()) {
            foundSchedule.orElse(null).setClass_name(schedule.getClass_name());
            foundSchedule.orElse(null).setGeneration(schedule.getGeneration());
            foundSchedule.orElse(null).setDepartment(schedule.getDepartment());
            scheduleRepository.save(foundSchedule.orElse(null));
            return true;
        }
        return false;
    }
    public void deleteSchedule(String id) {
        scheduleRepository.deleteById(id);
    }

}
