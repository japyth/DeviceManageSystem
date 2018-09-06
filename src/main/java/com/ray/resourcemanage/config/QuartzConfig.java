package com.ray.resourcemanage.config;

import com.ray.resourcemanage.task.DbTask;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

/**
 * @Description: 定时任务配置类
 */
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail dbTaskDetail(){
        return JobBuilder.newJob(DbTask.class).withIdentity("dbTask").storeDurably().build();
    }

    @Bean
    public CronTriggerFactoryBean cronTriggerBean(){
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean ();
        trigger.setJobDetail (dbTaskDetail());
        try {
            trigger.setCronExpression ("0 0 2 * * ?");//每天凌晨两点执行一次
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return trigger;

    }

}