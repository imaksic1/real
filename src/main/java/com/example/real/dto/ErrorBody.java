package com.example.real.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.LinkedList;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorBody {
    private LinkedList<String> username;
    private LinkedList<String> email;
    private LinkedList<String> password;

    public LinkedList<String> getUsername() {
        return username;
    }

    public void setUsername(LinkedList<String> username) {
        this.username = username;
    }

    public LinkedList<String> getEmail() {
        return email;
    }

    public void setEmail(LinkedList<String> email) {
        this.email = email;
    }

    public LinkedList<String> getPassword() {
        return password;
    }

    public void setPassword(LinkedList<String> password) {
        this.password = password;
    }
}
