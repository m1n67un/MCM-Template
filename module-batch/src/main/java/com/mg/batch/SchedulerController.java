package com.mg.batch;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class SchedulerController {

	private final Map<String, Object> dbMap = new HashMap<>();
    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void start() {
        try {

		} catch (Exception e) {
			log.error("Error occured when scheduling a job", e);
		}
    }

    public Trigger buildJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
                .build();
    }

    public JobDetail buildJobDetail(Class<? extends Job> jobClass, String name, String group, Map<String, Object> params) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(name, group)
                .setJobData(new JobDataMap(params))
                .build();
    }
}