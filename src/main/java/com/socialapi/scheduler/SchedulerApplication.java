package com.socialapi.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
@SpringBootApplication
public class SchedulerApplication {

	
	@Autowired
	private  Environment env;
	
	public static void main(String[] args) {
		
		
		SpringApplication.run(SchedulerApplication.class, args);
		
		
		
		
	}
	@Bean
	public JobDetail sampleJobDetail() {
		return JobBuilder.newJob(SampleJob.class).withIdentity("sampleJob")
				.usingJobData("name", "World").storeDurably().build();
	}

	@Bean
	public Trigger sampleJobTrigger() {
		
		String timetorun =env.getProperty("timetoruninminutes");
		
		int time = Integer.parseInt(timetorun);
		
		
		
		timetorun=env.getProperty("timetorun");
		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(time).repeatForever();// specifying then hours for scheduling the job..
		
		
//	withIntervalInHours(24)  --  for running the application in 24 hours schedule.
		
		

		return TriggerBuilder.newTrigger().forJob(sampleJobDetail())
				.withIdentity("sampleTrigger").withSchedule(scheduleBuilder).build();
	}
}
