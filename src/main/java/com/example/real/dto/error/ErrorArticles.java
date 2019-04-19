package com.example.real.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.LinkedList;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorArticles {
    private LinkedList<String> title;
    private LinkedList<String> body;
    private LinkedList<String> description;
}
