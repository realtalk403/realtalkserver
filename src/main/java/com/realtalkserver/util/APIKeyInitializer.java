package com.realtalkserver.util;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class APIKeyInitializer implements ServletContextListener {
    static final String ATTRIBUTE_ACCESS_KEY = "apiKey";

    private static final String API_KEY = "AIzaSyBXfY3tvJ8tX4ia7mFsbpBZSOQeONWa95c";

    private final Logger logger = Logger.getLogger(getClass().getName());

    public void contextInitialized(ServletContextEvent event) {
        logger.info("Loading API Key for Google Cloud Messaging");
        String key = getKey();
        event.getServletContext().setAttribute(ATTRIBUTE_ACCESS_KEY, key);
    }

    /**
     * Gets the access key.
     */
    protected String getKey() {
        return API_KEY;
    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}
