package com.github.mhewedy.jasync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

class SpringUtil {

    private static final boolean FOUND_SPRING_EXECUTOR = isClassPresent("org.springframework.core.task.TaskExecutor");

    /**
     * try to find a bean with name jasyncTaskExecutor of type {@link TaskExecutor}
     */
    static Executor getSpringTaskExecutor() {
        if (!FOUND_SPRING_EXECUTOR) {
            return null;
        }
        return AppContextUtil.findBean("jasyncTaskExecutor", TaskExecutor.class);
    }


    private static boolean isClassPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    @Slf4j
    @Component
    private static class AppContextUtil implements ApplicationContextAware {
        private static ApplicationContext context;

        public static <T> T findBean(String name, Class<T> bean) {
            if (context == null) {
                log.trace("no spring context found");
                return null;
            }
            try {
                return context.getBean(name, bean);
            } catch (NoSuchBeanDefinitionException ex) {
                log.trace("no such bean: " + ex.getMessage());
                return null;
            }
        }

        @Override
        public void setApplicationContext(ApplicationContext ac) {
            context = ac;
        }
    }
}
