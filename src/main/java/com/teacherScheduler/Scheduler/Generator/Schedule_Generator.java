package com.teacherScheduler.Scheduler.Generator;

import com.teacherScheduler.Scheduler.Generator.Course;
import com.teacherScheduler.Scheduler.Generator.DataContainer;
import com.teacherScheduler.Scheduler.Generator.ScheduleGenerate;
import com.teacherScheduler.Scheduler.Generator.SchoolClass;
import com.teacherScheduler.Scheduler.Generator.Session;
import com.teacherScheduler.Scheduler.Generator.SessionPeriod;
import com.teacherScheduler.Scheduler.Generator.Teacher;
import com.teacherScheduler.Scheduler.model.Schedule;

import java.util.*;


public class Schedule_Generator {

    //public static void sort() {
    //
    //}

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
                List<Teacher> courseTeachers = course.getTeachers(availability);
                List<Teacher> availableTeachers = new ArrayList<>();

                // Retrieve only teacher which can be able to assign to class
                for (Teacher teacher : courseTeachers) {
                    if (teachers.contains(teacher)) {
                        availableTeachers.add(teacher);
                    }
                }

                // Pick random teacher
                Teacher teacher;
                if (availableTeachers.isEmpty()) {
                    teacher = getRandomElement(teachers);
                }
                else {
                    teacher = getRandomElement(availableTeachers);
                }
                // If teacher already teach 5 class
                while (teacher.getNumberOfTeachingClass() <= 0) {

                    if (!availableTeachers.isEmpty()) {
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

                    if (!availableTeachers.isEmpty()) {
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
    private static List<SchoolClass> generateClasses(int numberOfTeachers, String classPrefix, List<Course> courses, int classNumber) {
        // number of class = number of teachers
        if (numberOfTeachers % 5 != 0) {
            throw new IllegalArgumentException();
        }
        List<SchoolClass> schoolClasses = new ArrayList<>();

        for (int i = 0; i < numberOfTeachers; i++) {
            int classNumberPefix = classNumber + i;
            schoolClasses.add(new SchoolClass(classPrefix + classNumberPefix, courses));
        }
        return schoolClasses;
    }

    // Generate schedule
    public static List<List<DataContainer>> generateSchedule(List<Course> courses, List<Teacher> allTeachers) {

        // Divide teacher base on availability
        Map<String, List<Teacher>> teacherAvailability = divideTeacherOnAvailability(allTeachers);

        List<ScheduleGenerate> schedules = new ArrayList<>();
        List<List<DataContainer>> test = new ArrayList<>();


        for (Map.Entry<String, List<Teacher>> entry : teacherAvailability.entrySet()) {

            String availability = entry.getKey();
            List<Teacher> teachers = entry.getValue();


            int startIndex = 0;
            int lastIndex = 5;
            while (true) {

                // Get list of class 5 at a time
                List<Teacher> teacherList;

                try {
                    //classes = schoolClasses.subList(startIndex, lastIndex);
                    teacherList = teachers.subList(startIndex, lastIndex);
                    System.out.println(availability);
                    for (Teacher teacher : teacherList) {
                        System.out.println(teacher.getTeacherName());
                    }
                }
                catch (IndexOutOfBoundsException e) {
                    //continue;
                    System.out.println("Invalid number of teachers");
                    break;
                }

                List<SchoolClass> schoolClasses;

                // Generate class base on teacher
                try {
                    schoolClasses = generateClasses(teacherList.size(), availability.substring(0, 1), courses, startIndex + 1);
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Invalid number of teacher");
                    return test;
                }

                try {
                    Schedule_Generator.assignCourse(schoolClasses, courses, teacherList, availability);
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Invalid number of teacher");
                    return test;
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

                        // List of all teachers that available for that time
                        List<Teacher> availableTeachers = new ArrayList<>(teacherList);

                        // For each class
                        for (SchoolClass schoolClass : schoolClasses) {

                            // Get random teacher from all teachers who teach this particular class
                            Teacher teacher = getRandomElement(schoolClass.getAllTeachers());

                            // If teacher not available
                            while (!availableTeachers.contains(teacher)) {
                                teacher = getRandomElement(schoolClass.getAllTeachers());
                                System.out.println(teacher.getTeacherName());
                            }

                            // If he/she have already teaches that class 2 session
                            while (teacher.getClassTeachingSession(schoolClass) <= 0) {
                                teacher = getRandomElement(schoolClass.getAllTeachers());
                                System.out.println("Hello");
                            }

                            // What course he/she teach
                            Course course = schoolClass.getCourseByTeacher(teacher);

                            // map class to course
                            session.addSession(schoolClass, course);

                            // Teacher is no longer available for this period
                            availableTeachers.remove(teacher);

                        }
                        // Add session to period
                        sessionPeriod.addSessionPeriod(period, session);
                        // Add to schedule
                        schedule.addSchedulePeriod(day, sessionPeriod);
                    }
                    System.out.println("next day");
                }
                schedules.add(schedule);
                test.add(schedule.gettingSchedule());
                startIndex += 5;
                lastIndex += 5;
            }
            System.out.println("next time of days");
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
            }
        }

        return test;
    }


}
