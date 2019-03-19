package com.example.real.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;

@Data
public class ErrorSignInText {
    private HashMap<String, LinkedList<String>> errors;
}
