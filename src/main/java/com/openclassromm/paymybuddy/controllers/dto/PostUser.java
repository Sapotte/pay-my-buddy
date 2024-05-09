package com.openclassromm.paymybuddy.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Getter
@Setter
public class PostUser {

    public String username;

    public String email;

    public String password;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
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
                "userName='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostUser postUser = (PostUser) o;
        return Objects.equals(username, postUser.username) && Objects.equals(email, postUser.email) && Objects.equals(password, postUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password);
    }
}
