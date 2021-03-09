package com.library.api.resources;

import com.library.api.DTOs.BookDTO;
import com.library.api.domain.Book;
import com.library.api.exceptions.ApiErrors;
import com.library.api.exceptions.BussinesException;
import com.library.api.services.IBookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public BookDTO createBook(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map( dto, Book.class );
        Book createdBook = service.save(entity);

        return modelMapper.map( entity, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
       BindingResult bindingResult = ex.getBindingResult();
       return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BussinesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinesException(BussinesException ex) {
        return new ApiErrors(ex);
    }
}
