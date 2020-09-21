package com.shop.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public abstract class H2PropertyReader {
    private static final Logger logger = LogManager.getLogger(H2PropertyReader.class);

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(DatabasePropertyReader.class.getClassLoader().getResourceAsStream("database/h2.properties"));
        } catch (IOException e) {
            logger.error("Error reading property for test database", e);
        }
    }

    public static String readUrl() {
        return properties.getProperty("h2.url");
    }

    public static String readScript() {
        return properties.getProperty("h2.script");
    }
}
