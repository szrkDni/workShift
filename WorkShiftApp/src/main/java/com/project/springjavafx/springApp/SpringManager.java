package com.project.springjavafx.springApp;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringManager {

    private static ConfigurableApplicationContext ctx;
    public static void stopSpringApp(){
        ctx.close();
    }
    public static void startSpringApp(){
        ctx = SpringApplication.run(SpringJavaFxApplication.class, "");
    }

    public static SpringJavaFxApplication getSpringApp(){
        String[] springBootAppBeanName = ctx.getBeanNamesForAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class);
        return (SpringJavaFxApplication) ctx.getBean(springBootAppBeanName[0]);
    }

}
