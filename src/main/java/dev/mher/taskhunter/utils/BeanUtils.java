package dev.mher.taskhunter.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * User: MheR
 * Date: 12/6/19.
 * Time: 1:47 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.utils.
 */

@Service
public class BeanUtils implements ApplicationContextAware {
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}