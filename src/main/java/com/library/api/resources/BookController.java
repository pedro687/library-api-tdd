package com.library.api.resources;

import com.library.api.DTOs.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook() {
        BookDTO dto = new BookDTO();
        dto.setAuthor("Jon Doe");
        dto.setId(1L);
        dto.setTitle("My book");
        dto.setIsbn("123123");

        return dto;
    }
}
