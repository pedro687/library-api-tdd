package com.library.api.DTOs;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {


    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private String isbn;


}
