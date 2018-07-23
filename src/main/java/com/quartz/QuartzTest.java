package com.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        try {
            JobDataMap jobDataMap = new JobDataMap();
            schedulerUtil.setJobDataMap(jobDataMap);
            schedulerUtil.setJobClass(TestJob.class);
            schedulerUtil.executeJobWithRepeatForever(0,1,0);
        }catch (Exception e){
            logger.error("任务调度异常",e);
        }
    }

    public static void main(String[] args) {
        test();
    }
}
