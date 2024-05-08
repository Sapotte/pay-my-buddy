package com.openclassromm.paymybuddy.controllers.dto;

import com.openclassromm.paymybuddy.db.models.User;

public class PostInternTransaction {
    private User friend;
    private Float amount;
    private String label;

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
