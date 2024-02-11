package com.teacherScheduler.Scheduler.Generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Course {
    private final String courseName;
    private final List<Teacher> teachers;

    public Course(String courseName) {
        this.courseName = courseName;
        this.teachers = new ArrayList<>();
    }
    // Add teacher who teach this course
    public void addTeacher(Teacher teacher) {
        if(!teachers.contains(teacher)) {
            this.teachers.add(teacher);
        }
    }
    public List<Teacher> getTeachers(String availability) {
        List<Teacher> availableTeachers = new ArrayList<>();

        // Get only teacher which available in certain time
        if (availability.equals("Morning")) {
            for (Teacher teacher : teachers) {
                if (teacher.isMorning()) {
                    availableTeachers.add(teacher);
                }
            }
        }
        else if (availability.equals("Afternoon")) {
            for (Teacher teacher : teachers) {
                if (teacher.isAfternoon()) {
                    availableTeachers.add(teacher);
                }
            }
        }
        else if (availability.equals("Evening")) {
            for (Teacher teacher : teachers) {
                if (teacher.isEvening()) {
                    availableTeachers.add(teacher);
                }
            }
        }

        return availableTeachers;
    }
    public String getCourseName() {
        return courseName;
    }

}
