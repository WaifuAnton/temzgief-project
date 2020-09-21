package com.shop.reader;

import com.shop.config.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public abstract class DatabasePropertyReader {
    private static final Logger logger = LogManager.getLogger(DatabasePropertyReader.class);

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(DatabasePropertyReader.class.getClassLoader().getResourceAsStream("database/database.properties"));
        } catch (IOException e) {
            logger.fatal("Error reading property file", e);
            System.exit(-1);
        }
    }

    public static String readType() {
        return properties.getProperty("database.type");
    }

    public static String readUser() {
        return properties.getProperty("database.user");
    }

    public static String readPassword() {
        return properties.getProperty("database.password");
    }

    public static String readUrl() {
        return properties.getProperty("database.url");
    }

    public static String readPoolSize() {
        return properties.getProperty("database.pool.size", Constants.DEFAULT_DB_POOL_SIZE);
    }

    private DatabasePropertyReader() {
        //can`t create an instance
    }
}
