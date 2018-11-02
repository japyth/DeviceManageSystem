package com.ray.resourcemanage.config;

import com.ray.resourcemanage.logManage.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class SpringStartUp {

    @Autowired
    private LogService logService;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        logService.logInfo("设备管理平台启动");
    }

    @PreDestroy
    public void destroy() {
        logService.logInfo("设备管理平台关闭");
    }
}
