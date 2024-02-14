package com.teacherScheduler.Scheduler.Generator;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ScheduleGenerate {
    // String = day (Monday, Tuesday...)
    String name;
    Map<String, List<SessionPeriod>> schedule;

    public ScheduleGenerate(String name) {
        this.name = name;
        this.schedule = new HashMap<>();
    }

    public void addSchedulePeriod(String day, SessionPeriod sessionPeriod) {
        this.schedule.computeIfAbsent(day, k -> new ArrayList<>()).add(sessionPeriod);
    }

    public void printSchedule() {
        System.out.println(name);
        schedule.forEach((day, sessionPeriodList) -> {
            System.out.println("Day: " + day);

            sessionPeriodList.forEach(sessionPeriod -> {
                sessionPeriod.period.forEach((periodNum, sessionList) -> {
                    System.out.println("  Period " + periodNum + ":");

                    sessionList.forEach(session -> {
                        session.getSession().forEach((schoolClass, course) ->
                                System.out.println("    Class: " + schoolClass.getClassName() +
                                        ", Course: " + course.getCourseName() +
                                        ", Teacher: " + schoolClass.getTeacherByCourse(course).getTeacherName()));
                    });
                });
            });

            System.out.println(); // Add a line break between days
        });
    }

    public List<DataContainer> gettingSchedule() {

        log.info("Schedule Generate Started");

        log.info("start loop to get data");
        List<DataContainer> t = new ArrayList<>();
        schedule.forEach((day, sessionPeriodList) -> {

            sessionPeriodList.forEach(sessionPeriod -> {

                sessionPeriod.period.forEach((periodNum, sessionList) -> {

                    sessionList.forEach(session -> {
                        session.getSession().forEach((schoolClass, course) -> {
                            DataContainer s = new DataContainer();
                            s.part_of_day = name;
                            s.day = day;
                            s.period = periodNum;
                            s.class_name = schoolClass.getClassName();
                            s.course = course.getCourseName();
                            s.teacher_id = schoolClass.getTeacherByCourse(course).getTeacherId();
                            s.teacher_name = schoolClass.getTeacherByCourse(course).getTeacherName();
                            t.add(s);

                            log.info(s.class_name + " " + s.course + " " + s.teacher_id);
                        });
                    });
                });
            });

        });

        log.info("End Schedule");

        return t;
    }
}