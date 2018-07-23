package com.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author miou
 * @date 2018/7/23
 */
public class TestJob implements Job{
    Logger log = LoggerFactory.getLogger(TestJob.class);

    @Override
    public void execute(JobExecutionContext arg) throws JobExecutionException {
        System.out.println(new Date());
    }
}
