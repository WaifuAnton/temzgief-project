package com.shop.reader;

import com.shop.config.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    private static final Logger logger = LogManager.getLogger(PropertyReader.class);

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(PropertyReader.class.getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            logger.fatal("Error reading property file", e);
            System.exit(-1);
        }
    }

    public static String readType() {
        return properties.getProperty("database.type", Constants.DEFAULT_DB);
    }

    public static String readUser() {
        return properties.getProperty("database.user");
    }

    public static String readPassword() {
        return properties.getProperty("database.password");
    }

    public static String readPort() {
        return properties.getProperty("database.port", Constants.DEFAULT_DB_PORT);
    }

    public static String readUrl() {
        return properties.getProperty("database.url", Constants.DEFAULT_DB_URL);
    }

    public static String readPoolSize() {
        return properties.getProperty("database.pool.size", Constants.DEFAULT_DB_POOL_SIZE);
    }

    private PropertyReader() {
        //can`t create an instance
    }
}
