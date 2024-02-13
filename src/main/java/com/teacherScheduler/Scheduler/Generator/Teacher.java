package com.teacherScheduler.Scheduler.Generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import  java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Teacher {
    private String id;
    private final String Name;
    @Getter
    private final List<Course> Teaching_Course;
    @Getter
    private int numberOfTeachingClass;
    private final Map<SchoolClass, Integer> teachingSession;

    private final boolean isMorning;
    private final boolean isAfternoon;
    private final boolean isEvening;

    public Teacher(String id, String name, boolean isMorning, boolean isAfternoon, boolean isEvening, List<Course> teaching_Course) {
        this.id = id;
        this.Name = name;
        //Availability = availability;
        this.isMorning = isMorning;
        this.isAfternoon = isAfternoon;
        this.isEvening = isEvening;

        // Number of class he/she teaches increase depend on how many times he/she teaches
        if (this.isMorning) {
            numberOfTeachingClass += 5;
        }
        if (this.isAfternoon) {
            numberOfTeachingClass += 5;
        }
        if (this.isEvening) {
            numberOfTeachingClass += 5;
        }

        this.teachingSession = new HashMap<>();
        this.Teaching_Course = new ArrayList<>();
        for (Course course : teaching_Course) {
            this.Teaching_Course.add(course);

            // Add this teacher to course
            course.addTeacher(this);
        }
    }

    public void reduceNumberOfTeachingClass() {
        this.numberOfTeachingClass--;
    }
    public String getTeacherName() {
        return Name;
    }

    public void setClassTeachingSession(SchoolClass schoolClass, int session) {
        this.teachingSession.put(schoolClass, session);
    }

    public Integer getClassTeachingSession(SchoolClass schoolClass) {
        return teachingSession.get(schoolClass);
    }

    public String getTeacherId() { return id; }
    public boolean isMorning() {
        return isMorning;
    }

    public boolean isAfternoon() {
        return isAfternoon;
    }

    public boolean isEvening() {
        return isEvening;
    }
}
