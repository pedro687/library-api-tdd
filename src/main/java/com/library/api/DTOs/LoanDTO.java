package com.library.api.DTOs;

import com.library.api.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
    private String isbn;

    private Long id;

    private String customer;

    private BookDTO book;
}
