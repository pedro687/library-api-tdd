package com.library.api.services.impl;

import com.library.api.domain.Loan;
import com.library.api.repositories.LoanRepository;
import com.library.api.services.ILoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService implements ILoanService {

    LoanRepository repo;

    @Autowired
    public LoanService(LoanRepository repo) {
        this.repo = repo;
    }

    @Override
    public Loan save(Loan save) {
        return repo.save(save);
    }
}
