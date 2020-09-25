package com.shop.config;

import com.shop.reader.DatabasePropertyReader;

public final class Constants {
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public static final String DEFAULT_DB_POOL_SIZE = "10";

    public static final String DATABASE_TYPE = DatabasePropertyReader.readType();
    public static final String MYSQL = "mysql";
    public static final int PRODUCT_LIMIT = 2;
    public static final String H2 = "h2";

    public static final String RANDOM_ALGORITHM = "SHA1PRNG";
    public static final int SALT_SIZE = 16;
    public static final String DIGEST_ALGORITHM = "SHA-256";
    public static final String SALT_ENCODING = "cp1251";

    private Constants() {
        //can`t create instance
    }
}
