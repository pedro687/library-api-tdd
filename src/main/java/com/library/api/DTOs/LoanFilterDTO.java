package com.library.api.DTOs;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanFilterDTO {
    private String isbn;
    private String customer;
}
