package com.library.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    private Long id;

    private String isbn;

    private Book book;

    private LocalDate loanDate;

    private Boolean returned;

    private String customer;
}
