package com.example.real.dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.LinkedList;

@Data
public class ErrorSignInBody {

    private LinkedList <String> emailOrPassword;

    public LinkedList<String> getEmailOrPassword() {
        return emailOrPassword;
    }

    public void setEmailOrPassword(LinkedList<String> emailOrPassword) {
        this.emailOrPassword = emailOrPassword;
    }
}
