package com.library.api.repositories;

import com.library.api.domain.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Page<Loan> findByBookIsbnOrCustomer(String isbn, String customer, Pageable pageable);
}
