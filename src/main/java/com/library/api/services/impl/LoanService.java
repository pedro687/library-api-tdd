package com.library.api.services.impl;

import com.library.api.DTOs.LoanFilterDTO;
import com.library.api.domain.Loan;
import com.library.api.repositories.LoanRepository;
import com.library.api.services.ILoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public Optional<Loan> getById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repo.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO dto, Pageable pageable) {
        return repo.findByBookIsbnOrCustomer(dto.getIsbn(), dto.getCustomer(), pageable);
    }

}
