package com.teacherScheduler.Scheduler.services;

import com.teacherScheduler.Scheduler.Generator.Course;
import com.teacherScheduler.Scheduler.Generator.DataContainer;
import com.teacherScheduler.Scheduler.Generator.Schedule_Generator;
import com.teacherScheduler.Scheduler.Generator.Teacher;
import com.teacherScheduler.Scheduler.dto.GroupByDTO;
import com.teacherScheduler.Scheduler.dto.ScheduleRequest;
import com.teacherScheduler.Scheduler.dto.ScheduleResponse;
import com.teacherScheduler.Scheduler.dto.TeacherDTO;
import com.teacherScheduler.Scheduler.dto.ViewScheduleResponse;
import com.teacherScheduler.Scheduler.model.Schedule;
import com.teacherScheduler.Scheduler.respository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MongoTemplate mongoTemplate;
    public void createSchedule(ScheduleRequest scheduleRequest) {
        List<Course> courses = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();
        for(String s : scheduleRequest.getCourses()) {
            courses.add(new Course(s));
        }

        for(TeacherDTO t : scheduleRequest.getTeachers()) {
            List<Course> temp = new ArrayList<>();
            for(String f : t.getTeach()) {
                temp.add(new Course(f));
            }
            teachers.add(new Teacher(
                    t.getId(),
                    t.getName(),
                    (t.getIsMorning() == 1),
                    (t.getIsAfternoon() == 1),
                    (t.getIsEvening() == 1),
                    temp
            ));
        }
        log.info("Service working");
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
        log.info("Start Saving data");

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

    public List<GroupByDTO> getAllSchedules() {
        GroupOperation groupOperation = Aggregation.group("Name", "generation", "department");

        TypedAggregation<Schedule> aggregation = Aggregation.newAggregation(Schedule.class, groupOperation);

        List<Schedule> result =  mongoTemplate.aggregate(aggregation, Schedule.class, Schedule.class).getMappedResults();

        List<GroupByDTO> group = new ArrayList<>();
        for(Schedule s : result) {
            GroupByDTO g = new GroupByDTO();

            g.setName(s.getId().split(",")[0].split(":")[1].replaceAll("\"", "").replaceAll(" ", ""));
            g.setGeneration(s.getId().split(",")[1].split(":")[1].replaceAll("\"", "").replaceAll(" ", ""));
            g.setDepartment(s.getId().split(",")[2].split(":")[1].replaceAll("\"", "").replaceAll(" ", "").replaceAll("}", ""));


            log.info(s.getId());
            group.add(g);
        }

        return group;
    }

    public ScheduleResponse mapToScheduleResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .class_name(schedule.getClass_name())
                .department(schedule.getDepartment())
                .generation(schedule.getGeneration())
                .part_of_day(schedule.getPart_of_day())
                .day(schedule.getDay())
                .period(schedule.getPeriod())
                .course(schedule.getCourse())
                .teacher_id(schedule.getTeacher_id())
                .teacher_name(schedule.getTeacher_name())
                .build();
    }

    public ScheduleResponse getSchedule(String id) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        return ScheduleResponse.builder()
                .id(schedule.orElse(null).getId())
                .class_name(schedule.orElse(null).getClass_name())
                .department(schedule.orElse(null).getDepartment())
                .generation(schedule.orElse(null).getGeneration())
                .part_of_day(schedule.orElse(null).getPart_of_day())
                .day(schedule.orElse(null).getDay())
                .period(schedule.orElse(null).getPeriod())
                .course(schedule.orElse(null).getCourse())
                .teacher_id(schedule.orElse(null).getTeacher_id())
                .teacher_name(schedule.orElse(null).getTeacher_name())
                .build();
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

    public List<ViewScheduleResponse> viewService(String name, String generation, String department) {

        Query query = new Query()
                .addCriteria((Criteria.where("Name").is(name)))
                .addCriteria((Criteria.where("department").is(department)))
                .addCriteria((Criteria.where("generation").is(generation)));

        List<Schedule> schedules = mongoTemplate.find(query, Schedule.class);
        List<ViewScheduleResponse> res = new ArrayList<>();

        for(int i=0; i<schedules.size(); i++) {
            final ViewScheduleResponse sch = getViewScheduleResponse(schedules, i);
            res.add(sch);
        }

        return res;
    }

    private static ViewScheduleResponse getViewScheduleResponse(List<Schedule> schedules, int i) {
        ViewScheduleResponse sch = new ViewScheduleResponse();
        sch.setId(schedules.get(i).getId());
        sch.setGeneration(schedules.get(i).getGeneration());
        sch.setDepartment(schedules.get(i).getDepartment());
        sch.setDay(schedules.get(i).getDay());
        sch.setCourse(schedules.get(i).getCourse());
        sch.setClass_name(schedules.get(i).getClass_name());
        sch.setPeriod(schedules.get(i).getPeriod());
        sch.setPart_of_day(schedules.get(i).getPart_of_day());
        sch.setTeacher_id(schedules.get(i).getTeacher_id());
        sch.setTeacher_name(schedules.get(i).getTeacher_name());
        return sch;
    }

}
