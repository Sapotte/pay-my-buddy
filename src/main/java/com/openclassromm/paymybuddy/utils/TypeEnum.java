package com.openclassromm.paymybuddy.utils;

public enum TypeEnum {
    DEPOSIT("+ "), WITHDRAW("- ");

    private final String code;

    TypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
