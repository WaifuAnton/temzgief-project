package com.shop.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Status {
    REGISTERED, PAID, CANCELLED, IN_PROCESS;

    public List<Status> allowedTransactions() {
        switch (this) {
            case REGISTERED:
                return new ArrayList<>(Arrays.asList(PAID, CANCELLED));
            case IN_PROCESS:
                return new ArrayList<>(Arrays.asList(REGISTERED));
            default:
                return new ArrayList<>(0);
        }
    }
}
