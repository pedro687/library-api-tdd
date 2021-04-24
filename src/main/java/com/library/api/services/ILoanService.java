package com.library.api.services;

import com.library.api.domain.Loan;

import java.util.Optional;

public interface ILoanService {
    Loan save(Loan save);
    Optional<Loan> getById(Long id);

    Loan update(Loan loan);
}
