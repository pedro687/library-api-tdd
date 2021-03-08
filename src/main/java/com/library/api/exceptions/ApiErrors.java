package com.library.api.exceptions;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.ArrayList;
import java.util.List;

public class ApiErrors {
    private List<String> erros;

    public ApiErrors(BindingResult bindingResult) {
        this.erros = new ArrayList<>();

        bindingResult.getAllErrors().forEach(error -> this.erros.add(error.getDefaultMessage()));
    }

    public List<String> getErros() {
        return erros;
    }
}
