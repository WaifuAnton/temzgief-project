package com.shop.reader;

import com.shop.config.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class MySQLPropertyReader {
    private static final Logger logger = LogManager.getLogger(MySQLPropertyReader.class);

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(MySQLPropertyReader.class.getClassLoader().getResourceAsStream("mysql.properties"));
        } catch (IOException e) {
            logger.fatal("Error reading property file", e);
            System.exit(-1);
        }
    }

    public static String readUser() {
        return properties.getProperty("mysql.user");
    }

    public static String readPassword() {
        return properties.getProperty("mysql.password");
    }

    public static String readPort() {
        return properties.getProperty("mysql.port", Constants.DEFAULT_DB_PORT);
    }

    public static String readUrl() {
        return properties.getProperty("mysql.url", Constants.DEFAULT_DB_URL);
    }

    public static String readPoolSize() {
        return properties.getProperty("mysql.pool.size", Constants.DEFAULT_DB_POOL_SIZE);
    }

    private MySQLPropertyReader() {
        //can`t create an instance
    }
}
