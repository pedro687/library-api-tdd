package com.library.api.resources;

import com.library.api.DTOs.BookDTO;
import com.library.api.domain.Book;
import com.library.api.services.IBookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private IBookService service;
    private ModelMapper modelMapper;

    public BookController(IBookService service, ModelMapper mapper) {
        this.service = service;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook(@RequestBody BookDTO dto) {
        Book entity = modelMapper.map( dto, Book.class );
        Book createdBook = service.save(entity);

        return modelMapper.map( entity, BookDTO.class);
    }
}
