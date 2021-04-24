package com.library.api.DTOs;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReturnedLoanDTO {
    private Boolean returned;
}
