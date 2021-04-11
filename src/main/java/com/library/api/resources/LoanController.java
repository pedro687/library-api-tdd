package com.library.api.resources;

import com.library.api.DTOs.LoanDTO;
import com.library.api.domain.Book;
import com.library.api.domain.Loan;
import com.library.api.services.impl.BookService;
import com.library.api.services.impl.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("api/loan")
public class LoanController {

    private final LoanService service;
    private final BookService bookService;

    @Autowired
    public LoanController(LoanService service, BookService bookService) {
        this.service = service;
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO loanDTO) {
        Book book = bookService.findByIsbn(loanDTO.getIsbn()).orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Book not found for passed isbn"
                ));

        Loan entity = Loan.builder().book(book).customer(loanDTO.getCustomer())
            .loanDate(LocalDate.now())
             .isbn(loanDTO.getIsbn())
            .build();

        entity = service.save(entity);

        return entity.getId();
    }
}
