package com.openclassromm.paymybuddy.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Getter
@Setter
public class PostUser {

    public String userName;

    public String email;

    public String password;

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PostUser{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostUser postUser = (PostUser) o;
        return Objects.equals(userName, postUser.userName) && Objects.equals(email, postUser.email) && Objects.equals(password, postUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, email, password);
    }
}
