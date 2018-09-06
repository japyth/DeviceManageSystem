package com.ray.resourcemanage.task;

import com.ray.resourcemanage.util.PropertiesUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @Description: 数据库定时任务，备份数据库
 */
public class DbTask extends QuartzJobBean {
    private Log log = LogFactory.getLog(this.getClass());
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Properties props = PropertiesUtil.readProperties("application.properties");
        try{
            String userName = (String) props.get("spring.datasource.username");
            String password = (String) props.get("spring.datasource.password");
            String host = (String) props.get("datasource.host");
            String port = (String) props.get("datasource.port");
            String dbName = (String) props.get("datasource.dbName");
            String dirPath = (String) props.get("datasource.dirPath");
            Date date = new Date();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime = sd.format(date);
            String dumpFile = props.get("datasource.dumpFile") + dateTime;
            //拼接pg导出数据的sql脚本，一定需要cmd /c才能配合dirPath
            StringBuilder command = new StringBuilder("cmd /c pg_dump \"host=%s port=%s user=%s password=%s dbname=%s\" >%s");
            String dumpComand = String.format(command.toString(),host,port,userName,password,dbName,dumpFile);
            //此处dirPath是指工作目录
            Runtime.getRuntime().exec(dumpComand, null, new File(dirPath));
        } catch (Exception e) {
            log.info(e);
        }
    }
}