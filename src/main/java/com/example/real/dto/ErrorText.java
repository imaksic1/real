package com.example.real.dto;

import lombok.Data;

@Data
public class ErrorText
{
    private ErrorBody error;

    public ErrorBody getError() {
        return error;
    }

    public void setError(ErrorBody error) {
        this.error = error;
    }
}
