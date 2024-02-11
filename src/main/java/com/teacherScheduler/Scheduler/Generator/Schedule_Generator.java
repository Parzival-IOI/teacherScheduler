package com.teacherScheduler.Scheduler.Generator;

import com.teacherScheduler.Scheduler.model.Schedule;
import com.teacherScheduler.Scheduler.respository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
public class Schedule_Generator {
    private static ScheduleRepository scheduleRepository;

    // Get random index from list
    private static <T> T getRandomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }

        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    // For each class, assign teacher to courses
    public static void assignCourse(List<SchoolClass> classes, List<Course> courses, List<Teacher> teachers, String availability) {

        if (teachers.size() < 5) {
            throw new IllegalArgumentException();
        }

        for (Course course : courses) {

            for (SchoolClass schoolClass : classes) {

                // List of teachers who can teach that course
                List<Teacher> availableTeachers = course.getTeachers(availability);

                // Pick random teacher
                Teacher teacher;
                if (availableTeachers == null || availableTeachers.isEmpty()) {
                    teacher = getRandomElement(teachers);
                }
                else {
                    teacher = getRandomElement(availableTeachers);
                }
                // If teacher already teach 5 class
                while (teacher.getNumberOfTeachingClass() <= 0) {

                    if (availableTeachers != null) {
                        availableTeachers.remove(teacher);
                    }

                    try {
                        teacher = getRandomElement(availableTeachers);
                    }
                    catch (IllegalArgumentException e) {
                        teacher = getRandomElement(teachers);
                    }
                }

                // If teacher has been assign to the class yet
                while (schoolClass.isTheTeacherAssigned(teacher)) {

                    if (availableTeachers != null) {
                        availableTeachers.remove(teacher);
                    }

                    try {
                        teacher = getRandomElement(availableTeachers);
                    }
                    catch (IllegalArgumentException e) {
                        teacher = getRandomElement(teachers);
                    }
                }

                // If no teacher have been assign to that course yet
                if (schoolClass.getAssignedTeacher(course) == null) {

                    // Assign the teacher
                    schoolClass.assignTeacher(course, teacher);
                    teacher.setClassTeachingSession(schoolClass, 2);
                    teacher.reduceNumberOfTeachingClass();
                }
            }

        }
    }

    // Divide teacher to see who available on when
    public static Map<String, List<Teacher>> divideTeacherOnAvailability(List<Teacher> teachers) {
        Map<String, List<Teacher>> teacherAvailability = new HashMap<>();
        for (Teacher teacher : teachers) {
            if (teacher.isMorning()) {
                addToMap(teacherAvailability, "Morning", teacher);
            }
            if (teacher.isAfternoon()) {
                addToMap(teacherAvailability, "Afternoon", teacher);
            }
            if (teacher.isEvening()) {
                addToMap(teacherAvailability, "Evening", teacher);
            }
        }
        return teacherAvailability;
    }

    // Helper method to add a teacher to the map
    private static void addToMap(Map<String, List<Teacher>> map, String key, Teacher teacher) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<>());
        }
        map.get(key).add(teacher);
    }

    // Create morning class, afternoon, evening
    private static List<SchoolClass> generateClasses(int numberOfTeachers, String classPrefix, List<Course> courses) {
        // number of class = number of teachers

        List<SchoolClass> schoolClasses = new ArrayList<>();

        for (int i = 0; i < numberOfTeachers; i++) {
            schoolClasses.add(new SchoolClass(classPrefix + (i+1) , courses));
        }
        return schoolClasses;
    }

    // Generate schedule
    public static void generateSchedule(List<Course> courses, List<Teacher> allTeachers, String Generation, String Department) {

        // Divide teacher base on availability
        Map<String, List<Teacher>> teacherAvailability = divideTeacherOnAvailability(allTeachers);

        List<ScheduleGenerate> schedules = new ArrayList<>();

        List<List<DataContainer>> test = new ArrayList<>();

        for (Map.Entry<String, List<Teacher>> entry : teacherAvailability.entrySet()) {

            String availability = entry.getKey();
            List<Teacher> teachers = entry.getValue();

            // Generate class base on teacher
            List<SchoolClass> schoolClasses = generateClasses(teachers.size(), availability.substring(0, 1), courses);

            try {
                Schedule_Generator.assignCourse(schoolClasses, courses, teachers, availability);
            }
            catch (IllegalArgumentException e) {
                System.out.println("Number of teachers in " + availability + " is only " + teachers.size() + ". You need at least 5 teacher to make valid assignment of class");
                return;
            }

            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            Integer[] periods = {1, 2};

            // Initialize new schedule
            ScheduleGenerate schedule = new ScheduleGenerate(availability);

            for (String day : days) {
                for (int period : periods) {

                    // Initialize new period
                    SessionPeriod sessionPeriod = new SessionPeriod();

                    // Create new session
                    Session session = new Session();

                    // List of all teachers for that time (Morning, Afternoon ...)
                    List<Teacher> availableTeachers = new ArrayList<>(teachers);

                    // For each class
                    for (SchoolClass schoolClass : schoolClasses) {

                        Teacher teacher = getRandomElement(schoolClass.getAllTeachers());

                        // If teacher not available
                        while (!availableTeachers.contains(teacher)) {
                            teacher = getRandomElement(schoolClass.getAllTeachers());
                        }

                        // If he/she have already teaches that class 2 session
                        while (teacher.getClassTeachingSession(schoolClass) <= 0) {
                            teacher = getRandomElement(schoolClass.getAllTeachers());
                        }

                        // What course he/she teach
                        Course course = schoolClass.getCourseByTeacher(teacher);

                        // Create new session and map class to course

                        session.addSession(schoolClass, course);

                        // Teacher is no longer available for this period
                        availableTeachers.remove(teacher);

                    }
                    // Add session to period
                    sessionPeriod.addSessionPeriod(period, session);

                    // Add to schedule
                    schedule.addSchedulePeriod(day, sessionPeriod);
                }
            }
            schedules.add(schedule);
            test.add(schedule.gettingSchedule());
        }

        for(List<DataContainer> t : test) {
            for(DataContainer r : t) {

                Schedule schedule = new Schedule();
                schedule.setClass_name(r.class_name);
                schedule.setCourse(r.course);
                schedule.setPeriod(r.period);
                schedule.setPart_of_day(r.part_of_day);
                schedule.setTeacher_name(r.teacher_name);
                schedule.setTeacher_id(r.teacher_id);
                schedule.setDay(r.day);
                schedule.setGeneration(Generation);
                schedule.setDepartment(Department);

                scheduleRepository.save(schedule);
                log.info(schedule.getClass_name());
                System.out.println();
                System.out.print("class Name: " + r.class_name);
                System.out.print(", Course: " + r.course);
                System.out.print(", period: " + r.period);
                System.out.print(", Part of day: " + r.part_of_day);
                System.out.print(", teacher Name: " + r.teacher_name);
                System.out.print(", teacher ID: " + r.teacher_id);
                System.out.print(", day: " + r.day);
            }
        }
    }
}
