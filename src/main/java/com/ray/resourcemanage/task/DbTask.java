package com.ray.resourcemanage.task;

import com.ray.resourcemanage.util.PropertiesUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @Description: 数据库定时任务，备份数据库
 */

@Component
@EnableScheduling
public class DbTask {
    private Log log = LogFactory.getLog(this.getClass());
    @Scheduled(cron = "0 0 2 * * ?")
    protected void executeInternal() {
        log.info("定时任务开始");
        Properties props = PropertiesUtil.readProperties("application.properties");
        try {
            String userName = (String) props.get("spring.datasource.username");
            String password = (String) props.get("spring.datasource.password");
            String host = (String) props.get("datasource.host");
            String port = (String) props.get("datasource.port");
            String dbName = (String) props.get("datasource.dbName");
            String dirPath = (String) props.get("datasource.dirPath");
            Date date = new Date();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime = sd.format(date);
            String dumpFile = props.get("datasource.dumpFile") + "\\" + "backup_" +dateTime + ".sql";
            //拼接pg导出数据的sql脚本，一定需要cmd /c才能配合dirPath
            StringBuilder command = new StringBuilder("cmd /c pg_dump \"host=%s port=%s user=%s password=%s dbname=%s\" >%s");
            String dumpComand = String.format(command.toString(), host, port, userName, password, dbName, dumpFile);
            //此处dirPath是指工作目录
            Runtime.getRuntime().exec(dumpComand, null, new File(dirPath));
        } catch (Exception e) {
            log.error(e);
        }
    }
}