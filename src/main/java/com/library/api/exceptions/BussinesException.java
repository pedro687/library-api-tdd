package com.library.api.exceptions;

import com.library.api.domain.Book;

public class BussinesException extends RuntimeException {
    public BussinesException(String err) {
        super(err);
    }
}
