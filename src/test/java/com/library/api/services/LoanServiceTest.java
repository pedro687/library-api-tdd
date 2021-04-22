package com.library.api.services;

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

}