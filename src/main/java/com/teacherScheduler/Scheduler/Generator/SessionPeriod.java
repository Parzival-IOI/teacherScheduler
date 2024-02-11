package com.teacherScheduler.Scheduler.Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionPeriod {
    Map<Integer, List<Session>> period;

    public SessionPeriod() {
        this.period = new HashMap<>();
    }

    public void addSessionPeriod(int period, Session session) {
        this.period.computeIfAbsent(period, k -> new ArrayList<>()).add(session);
    }

    public List<Session> getPeriod(int periodNum) {
        return period.get(periodNum);
    }

}