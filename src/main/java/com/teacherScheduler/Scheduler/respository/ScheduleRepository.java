package com.teacherScheduler.Scheduler.respository;

import com.teacherScheduler.Scheduler.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
}
