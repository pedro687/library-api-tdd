package com.library.api.resources;

import com.library.api.DTOs.BookDTO;
import com.library.api.DTOs.LoanDTO;
import com.library.api.DTOs.LoanFilterDTO;
import com.library.api.DTOs.ReturnedLoanDTO;
import com.library.api.domain.Book;
import com.library.api.domain.Loan;
import com.library.api.services.impl.BookService;
import com.library.api.services.impl.LoanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/loan")
public class LoanController {

    private final LoanService service;
    private final BookService bookService;
    private final ModelMapper mapper;

    @Autowired
    public LoanController(LoanService service, BookService bookService, ModelMapper mapper) {
        this.service = service;
        this.bookService = bookService;
        this.mapper = mapper;
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

    @PatchMapping(value = "/{id}")
    public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto) {
        Loan loan = service.getById(id).get();
        loan.setReturned(dto.getReturned());
        service.update(loan);
    }

    @GetMapping
    public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageable) {
        Page<Loan> result = service.find(dto, pageable);
        List<LoanDTO> loans = result.getContent()
                .stream().map(entity -> {
                    Book book = entity.getBook();
                    BookDTO bookDTO = mapper.map(book, BookDTO.class);
                    LoanDTO loanDTO = mapper.map(entity, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
        }).collect(Collectors.toList());

        return new PageImpl<LoanDTO>(loans, pageable, result.getTotalElements());
    }
}
