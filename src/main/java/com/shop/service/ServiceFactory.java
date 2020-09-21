package com.shop.service;

import com.shop.config.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ServiceFactory {
    private static final Logger logger = LogManager.getLogger(ServiceFactory.class);

    public static Service getService(String serviceName) {
        switch (serviceName.toLowerCase()) {
            case Constants.REGISTER:
                return new RegisterService();
            default:
                logger.fatal("No service with name {}", serviceName);
                System.exit(-1);
                return null;
        }
    }
}
