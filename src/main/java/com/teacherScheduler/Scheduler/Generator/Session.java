package com.teacherScheduler.Scheduler.Generator;

import java.util.HashMap;
import java.util.Map;

// Map school class to course
public class Session {
    Map<SchoolClass, Course> session;

    public Session() {
        this.session = new HashMap<>();
    }

    public void addSession(SchoolClass schoolClass, Course course) {
        this.session.put(schoolClass, course);
    }

    public Map<SchoolClass, Course> getSession() {
        return session;
    }
}