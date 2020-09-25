package com.shop.service;

import com.shop.config.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {
    private static final Logger logger = LogManager.getLogger(ServiceFactory.class);

    private static ServiceFactory instance;

    private final Map<String, Service> services;

    public static synchronized ServiceFactory getInstance() {
        if (instance == null)
            instance = new ServiceFactory();
        return instance;
    }

    private ServiceFactory() {
        services = new HashMap<>();
        services.put("login", new LoginService());
        services.put("register", new RegisterService());
        services.put("women", new ProductsWomenService());
    }

    public Service getService(HttpServletRequest request) {
        return services.get(request.getParameter("action"));
    }
}
