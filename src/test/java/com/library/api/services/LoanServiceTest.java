package com.library.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.DTOs.ReturnedLoanDTO;
import com.library.api.domain.Book;
import com.library.api.domain.Loan;
import com.library.api.repositories.LoanRepository;
import com.library.api.services.impl.LoanService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanService service;

    @MockBean
    LoanRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new LoanService(repository);
    }

    @Test
    @DisplayName("Deve registrar um empréstimo")
    public void savedLoan() {
        Book book = Book.builder().id(1L).isbn("12345").title("Some Title").author("Jon Doe").build();

        Loan savingLoan = Loan.builder()
                .book(book)
                .customer("Jon Doe")
                .id(1L)
                .loanDate(LocalDate.now())
                .isbn("12345")
                .returned(false)
                .build();

        Loan savedLoan = Loan.builder()
                .book(book)
                .customer("Jon Doe")
                .id(1L)
                .loanDate(LocalDate.now())
                .isbn("12345")
                .returned(false)
                .build();

        Mockito.when(repository.save(savingLoan)).thenReturn(savedLoan);

        Loan saveLoan = service.save(savingLoan);

        Assertions.assertThat(saveLoan.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar emprestar um livro não retornado")
    public void loanedSaveTest() {
        Book book = Book.builder().id(1L).isbn("12345").title("Some Title").author("Jon Doe").build();

        Loan savingLoan = Loan.builder()
                .book(book)
                .customer("Jon Doe")
                .id(1L)
                .loanDate(LocalDate.now())
                .isbn("12345")
                .returned(false)
                .build();
    }

    @Test
    @DisplayName("Deve obter as informações de um empréstimo pelo id")
    public void getLoanDetailsTest() {
        Loan loan = Loan.builder().id(1L).customer("Jon Doe").isbn("12345").book(Book.builder().id(1L).isbn("12345").build())
                .loanDate(LocalDate.now())
                .build();

        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(loan));

        Optional<Loan> result = service.getById(1L);

        Assertions.assertThat(result.get().getId()).isEqualTo(1L);
    }
}
