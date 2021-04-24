package com.library.api.services;

import com.library.api.DTOs.LoanFilterDTO;
import com.library.api.domain.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ILoanService {
    Loan save(Loan save);
    Optional<Loan> getById(Long id);

    Loan update(Loan loan);
    Page<Loan> find(LoanFilterDTO dto, Pageable pageable);
}
