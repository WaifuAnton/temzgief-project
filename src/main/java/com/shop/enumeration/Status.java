package com.shop.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Status {
    REGISTERED, PAID, CANCELLED;

    public List<Status> allowedTransactions() {
        switch (this) {
            case REGISTERED:
                return new ArrayList<>(Arrays.asList(PAID, CANCELLED));
            default:
                return new ArrayList<>(0);
        }
    }
}
