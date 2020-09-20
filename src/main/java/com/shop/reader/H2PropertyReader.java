package com.shop.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class H2PropertyReader {
    private static final Logger logger = LogManager.getLogger(H2PropertyReader.class);

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(MySQLPropertyReader.class.getClassLoader().getResourceAsStream("h2.properties"));
        } catch (IOException e) {
            logger.fatal("Error reading property file", e);
            System.exit(-1);
        }
    }

    public static String readUrl() {
        return properties.getProperty("h2.url");
    }

    public static String readScript() {
        return properties.getProperty("h2.script");
    }
}
