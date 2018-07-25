package com.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author miou
 * @date 2018/7/23
 */
@DisallowConcurrentExecution
public class TestJob implements Job{
    Logger log = LoggerFactory.getLogger(TestJob.class);

    @Override
    public void execute(JobExecutionContext arg) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(Thread.currentThread().getName() +":"+ format.format(new Date()));
    }
}
