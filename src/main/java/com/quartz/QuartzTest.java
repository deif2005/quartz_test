package com.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * @author miou
 * @date 2018/7/23
 */

//Quartz API的关键接口是：
//Scheduler - 与调度程序交互的主要API。
//Job - 由希望由调度程序执行的组件实现的接口。
//JobDetail - 用于定义作业的实例。
//Trigger（即触发器） - 定义执行给定作业的计划的组件。
//JobBuilder - 用于定义/构建JobDetail实例，用于定义作业的实例。
//TriggerBuilder - 用于定义/构建触发器实例。
public class QuartzTest {
    private static Logger logger = LoggerFactory.getLogger(QuartzTest.class);

    public static void test(){
        SchedulerUtil schedulerUtil = new SchedulerUtil();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        System.out.println(calendar.getTime());
//        calendar.add(Calendar.SECOND,-30);
        calendar.add(Calendar.MINUTE,-2);
        System.out.println(calendar.getTime());
        Date date = calendar.getTime();
        try {
            JobDataMap jobDataMap = new JobDataMap();
            schedulerUtil.setJobDataMap(jobDataMap);
            schedulerUtil.setJobClass(TestJob.class);
//            schedulerUtil.setHours(1);
//            schedulerUtil.setMinutes(1);
            schedulerUtil.setSeconds(30);

            schedulerUtil.executeJobWithSimpleTrigger(date);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void simpleTest(){
        SchedulerUtil schedulerUtil = new SchedulerUtil();
        try {
            for (int i=0; i<5; i++){
                schedulerUtil.setJobClass(TestJob.class);
                schedulerUtil.executeJobWithRepeatForever(0,0,2);
            }
            Thread.sleep(5000);
        }catch (Exception e){
            logger.error("任务调度异常",e);
        }
    }

    public static void useJobNameStopTest(){
        SchedulerUtil schedulerUtil = new SchedulerUtil();
        Scheduler scheduler = null;
        schedulerUtil.setCronExpression("0/2 * * * * ? *");
//        schedulerUtil.setTriggerName("triggerName1");
//        schedulerUtil.setTriggerGroupName("triggerGroupName1");
        try {
            for (int i=0;i<5;i++){
                schedulerUtil.setJobClass(TestJob.class);
                schedulerUtil.setSeconds(2);
                schedulerUtil.executeJobWithSimpleTriggerByName("jobName"+i,"jobGroupName"+i);
//                scheduler = schedulerUtil.getScheduler();
            }
//            Thread.sleep(5000);
//            TriggerKey triggerKey = new TriggerKey("triggerName1", "triggerGroupName1");
//            scheduler.pauseTrigger(triggerKey);// 停止触发器
//            scheduler.unscheduleJob(triggerKey);// 移除触发器
//            for (int i=0;i<5;i++){
//                Thread.sleep(5000);
//                JobKey jobKey = new JobKey("jobName"+i, "jobGroupName"+i);
//                scheduler.interrupt(jobKey);
//                scheduler.deleteJob(jobKey);
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void useTriggerNameStopTest(){
        SchedulerUtil schedulerUtil = new SchedulerUtil();
        try {
            schedulerUtil.setJobClass(TestJob.class);
            schedulerUtil.executeJobWithCronTriggerByName("triggerName1","triggerGroupName1","0/2 * * * * ? *");
            Scheduler scheduler = schedulerUtil.getScheduler();
            TriggerKey triggerKey = new TriggerKey("triggerName1", "triggerGroupName1");
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        test();
//        useJobNameStopTest();
//        simpleTest();
//        useJobNameStopTest();
//        useTriggerNameStopTest();
    }

}
