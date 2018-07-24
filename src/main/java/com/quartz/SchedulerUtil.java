package com.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by defi on 2017-10-17.
 * 任务调度工具类
 */

public class SchedulerUtil {

    private String jobName;

    private String jobGroupName;

    private String triggerName;

    private String triggerGroupName;

    private Integer hours;

    private Integer minutes;

    private Integer seconds;

    private Class <? extends Job> jobClass;

    //任务开始时间
    private Date startDatetime;

    private boolean isStartNow=false;

    private String cronExpression;

    private JobDataMap jobDataMap;

    private boolean isRepeatForever=false;

    private Integer repeatCount=0;

    private Scheduler scheduler;

    /**
     * 指定时间执行一次任务
     * @param startDatetime
     * @throws Exception
     */
    public void executeJobWithSimpleTrigger(Date startDatetime){
        setStartDatetime(startDatetime);
        JobDetail job = getJobDetail();
        Trigger trigger = getTrigger();
        SchedulerExecutor(job,trigger);

//        JobDetail job = JobBuilder.newJob(jobClass).usingJobData(jobDataMap).build();
//        Trigger trigger = TriggerBuilder
//                .newTrigger()
//                .startAt(startDatetime)
//                .build();
//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.start();
//        scheduler.scheduleJob(job,trigger);
    }

    /**
     * 指定间隔：x时x分x秒执行一次
     * @param hours
     * @param minutes
     * @param seconds
     * @throws Exception
     */
    public void executeJobWithRepeatForever(int hours, int minutes, int seconds) {
        setHours(hours); setMinutes(minutes); setSeconds(seconds);
        JobDetail job = getJobDetail();
        Trigger trigger = getTrigger();
        SchedulerExecutor(job,trigger);

//        SimpleScheduleBuilder simpleScheduleBuilder = getSimpleScheduleBuilder();
//        JobDetail job = JobBuilder.newJob(jobClass).usingJobData(jobDataMap).build();
//        Trigger trigger = TriggerBuilder
//                .newTrigger()
//                .withSchedule(simpleScheduleBuilder)
//                .startNow()
//                .build();

//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.start();
//        scheduler.scheduleJob(job,trigger);
    }

    /**
     * 指定名称
     * @param jobName
     * @param jobGroupName
     * @throws Exception
     */
    public void executeJobWithSimpleTriggerByName(String jobName,String jobGroupName){
        setJobName(jobName); setJobGroupName(jobGroupName);
        JobDetail job = getJobDetail();
        Trigger trigger = getTrigger();
        SchedulerExecutor(job,trigger);

//        JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName,groupName).build();
//        Trigger trigger = TriggerBuilder
//                .newTrigger()
//                .withIdentity(triggerName, groupName)
//                .startAt(startDatetime)
//                .build();

//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.start();
//        scheduler.scheduleJob(job,trigger);
    }

    /**
     * 使用cron触发器执行
     * @throws Exception
     */
    public void executeJobWithCronTrigger(){
        JobDetail job = getJobDetail();
        Trigger trigger = getTrigger();
        SchedulerExecutor(job,trigger);

//        JobDetail job = JobBuilder.newJob(jobClass).build();
//        Trigger trigger = TriggerBuilder
//                .newTrigger()
//                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
//                .build();

//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.start();
//        scheduler.scheduleJob(job, trigger);
    }

    /**
     * 使用cron触发器 指定名称执行
     * @param jobName
     * @param jobGroupName
     * @throws Exception
     */
    public void executeJobWithCronTriggerByName(String jobName, String jobGroupName, String cronExpression){

        setJobName(jobName); setJobGroupName(jobGroupName); setCronExpression(cronExpression);
        JobDetail job = getJobDetail();
        Trigger trigger = getTrigger();
        SchedulerExecutor(job,trigger);

//        JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName, groupName).build();
//        Trigger trigger = TriggerBuilder
//                .newTrigger()
//                .withIdentity(triggerName, groupName)
//                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
//                .build();

//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.start();
//        scheduler.scheduleJob(job, trigger);
    }

    private ScheduleBuilder getScheduleBuilder(){
        ScheduleBuilder scheduleBuilder = null;
        if (cronExpression != null && !"".equals(cronExpression))
            scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        if (scheduleBuilder != null)
            return scheduleBuilder;

        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        if (hours > 0)
            simpleScheduleBuilder.withIntervalInHours(hours);
        if (minutes > 0)
            simpleScheduleBuilder.withIntervalInMinutes(minutes);
        if (seconds >0)
            simpleScheduleBuilder.withIntervalInSeconds(seconds);
        if (isRepeatForever)
            simpleScheduleBuilder.repeatForever();
        if (repeatCount > 0)
            simpleScheduleBuilder.withRepeatCount(repeatCount);
        return simpleScheduleBuilder;

    }

    private Trigger getTrigger(){

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();

        if (startDatetime != null)
            triggerBuilder.startAt(startDatetime);
        else if (isStartNow)
            triggerBuilder.startNow();
        else
            throw new RuntimeException("触发器未指定开始时间");

        TriggerKey triggerKey;
        if (triggerName != null && !"".equals(triggerName)){
            if (triggerGroupName != null && !"".equals(triggerGroupName))
                triggerKey = new TriggerKey(triggerName,triggerGroupName);
            else
                triggerKey = new TriggerKey(triggerName);
            triggerBuilder.withIdentity(triggerKey);
        }

        ScheduleBuilder ScheduleBuilder = getScheduleBuilder();
        triggerBuilder.withSchedule(ScheduleBuilder);

        Trigger trigger = triggerBuilder.build();

        return trigger;
    }

    private JobDetail getJobDetail(){

        if (jobClass == null)
            throw new RuntimeException("jobClass未指定");

        JobBuilder jobBuilder = JobBuilder.newJob(jobClass);
        JobKey jobKey;
        if (jobName != null && !"".equals(jobName)){
            if (jobGroupName != null && !"".equals(jobGroupName))
                jobKey = new JobKey(jobName,jobGroupName);
            else
                jobKey = new JobKey(jobName);
            jobBuilder.withIdentity(jobKey);
        }

        JobDetail job = jobBuilder.build();

        return job;
    }

    private void SchedulerExecutor(JobDetail job,Trigger trigger){
        if (scheduler==null)
            instanceScheduler();
        try {
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }

    private void instanceScheduler(){
        try {
            if (scheduler==null)
                this.scheduler = new StdSchedulerFactory().getScheduler();
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public boolean isRepeatForever() {
        return isRepeatForever;
    }

    public void setRepeatForever(boolean repeatForever) {
        isRepeatForever = repeatForever;
    }

    public Integer getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    public boolean isStartNow() {
        return isStartNow;
    }

    public void setStartNow(boolean startNow) {
        isStartNow = startNow;
    }

    public String getTriggerGroupName() {
        return triggerGroupName;
    }

    public void setTriggerGroupName(String triggerGroupName) {
        this.triggerGroupName = triggerGroupName;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

}
