package com.shop.config;

public final class Constants {
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public static final String DEFAULT_DB_POOL_SIZE = "10";

    public static final String MYSQL = "mysql";
    public static final String H2 = "h2";

    private Constants() {
        //can`t create instance
    }
}
