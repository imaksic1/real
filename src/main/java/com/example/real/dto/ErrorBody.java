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
}
