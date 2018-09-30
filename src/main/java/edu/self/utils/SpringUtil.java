package edu.self.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class SpringUtil {
	private static final ApplicationContext applicationContext = new XmlWebApplicationContext();
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
