package com.ray.resourcemanage.config;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebBindingInitializer;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class SpringMvcConfig implements WebBindingInitializer {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(true);
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
