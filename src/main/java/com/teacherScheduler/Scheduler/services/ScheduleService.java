package com.teacherScheduler.Scheduler.services;

import com.teacherScheduler.Scheduler.Generator.Course;
import com.teacherScheduler.Scheduler.Generator.DataContainer;
import com.teacherScheduler.Scheduler.Generator.Schedule_Generator;
import com.teacherScheduler.Scheduler.Generator.Teacher;
import com.teacherScheduler.Scheduler.dto.ScheduleRequest;
import com.teacherScheduler.Scheduler.dto.ScheduleResponse;
import com.teacherScheduler.Scheduler.dto.TeacherDTO;
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

//        log.info(scheduleRequest.getDepartment(), scheduleRequest.getGeneration());

        List<Course> courses = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();

        for(String s : scheduleRequest.getCourses()) {
            courses.add(new Course(s));
//            log.info(s);
        }

        for(TeacherDTO t : scheduleRequest.getTeachers()) {
            List<Course> temp = new ArrayList<>();
            for(String f : t.getTeach()) {
                temp.add(new Course(f));
//                log.info(f);

            }
            teachers.add(new Teacher(
                    t.getId(),
                    t.getName(),
                    (t.getIsMorning() == 1),
                    (t.getIsAfternoon() == 1),
                    (t.getIsEvening() == 1),
                    temp
            ));
//            log.info(Integer.toString(t.getIsMorning()));
//            log.info(Integer.toString(t.getIsAfternoon()));
//            log.info(Integer.toString(t.getIsEvening()));


        }

        List<List<DataContainer>> test = Schedule_Generator.generateSchedule(courses, teachers);
        List<Schedule> saveSchedule = new ArrayList<>();
        if(test == null) {
            log.info("null");
            return ;
        }
        int count = 1;
        for(List<DataContainer> t : test) {
            for(DataContainer r : t) {
                final Schedule schedule = getSchedule(scheduleRequest, r);
                schedule.setId(Integer.toString(count));
                saveSchedule.add(schedule);
                count++;
            }
        }
        log.info("over here");

        scheduleRepository.saveAll(saveSchedule);
    }

    private static Schedule getSchedule(ScheduleRequest scheduleRequest, DataContainer r) {
        Schedule schedule = new Schedule();
        schedule.setClass_name(r.class_name);
        schedule.setCourse(r.course);
        schedule.setPeriod(r.period);
        schedule.setPart_of_day(r.part_of_day);
        schedule.setTeacher_name(r.teacher_name);
        schedule.setTeacher_id(r.teacher_id);
        schedule.setDay(r.day);
        schedule.setGeneration(scheduleRequest.getGeneration());
        schedule.setDepartment(scheduleRequest.getDepartment());
        return schedule;
    }

    public List<ScheduleResponse> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();

        Schedule sch = new Schedule("1", "23", "cs", "E1", "Afternoon", "Monday", 1, "C#", "2", "Mick");

        scheduleRepository.save(sch);

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
