package com.openclassromm.paymybuddy.controllers.dto;

import com.openclassromm.paymybuddy.utils.TypeEnum;

public class PostExternTransaction {
    private Double amount;
    private String account;
    private TypeEnum type;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }
}
