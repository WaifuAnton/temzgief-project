package com.shop.security;

import com.shop.config.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public abstract class UserSecurity {
    private static final Logger logger = LogManager.getLogger(UserSecurity.class);

    private static final MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance(Constants.DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            logger.error("No digest algorithm {} found", Constants.DIGEST_ALGORITHM);
            throw new IllegalArgumentException(e);
        }
    }

    public static String generateSalt() {
        Random random;
        try {
            random = SecureRandom.getInstance(Constants.RANDOM_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            logger.warn("No secure algorithm {} found, setting up to default implementation", Constants.RANDOM_ALGORITHM);
            random = new Random();
        }
        try {
            byte[] bytes = new byte[Constants.SALT_SIZE];
            random.nextBytes(bytes);
            return new String(bytes, "cp1251");
        } catch (UnsupportedEncodingException e) {
            logger.error("No encoding algorithm {} found", Constants.SALT_ENCODING);
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] hashPassword(String password, String salt) {
        String finalString = salt.concat(password);
        return md.digest(finalString.getBytes());
    }
}
