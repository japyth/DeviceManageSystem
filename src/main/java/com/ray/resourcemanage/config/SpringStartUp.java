package com.ray.resourcemanage.config;

import com.ray.resourcemanage.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringStartUp {

    @Autowired
    private LogService logService;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        logService.logInfo("设备管理平台启动");
    }

}
