package com.library.api.resources;

import com.library.api.DTOs.BookDTO;
import com.library.api.domain.Book;
import com.library.api.exceptions.ApiErrors;
import com.library.api.exceptions.BussinesException;
import com.library.api.services.IBookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping(value = "/{id}")
    public BookDTO getBook(@PathVariable Long id) {
       return service.findById(id)
               .map(book -> modelMapper.map(book, BookDTO.class))
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        service.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.deleteById(id);
    }

    @PutMapping(value = "/{id}")
    public BookDTO updateBook(@PathVariable Long id, BookDTO dto) {
        return service.findById(id).map(book -> {
            book.setAuthor(dto.getAuthor());
            book.setTitle(dto.getTitle());
            book = service.update(book);

            return new ModelMapper().map(book, BookDTO.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Page<BookDTO> find(BookDTO bookDTO, Pageable pageRequest) {
        Book filter = modelMapper.map(bookDTO, Book.class);
        return service.find(filter, pageRequest).map(entity -> modelMapper.map(entity, BookDTO.class));
    }

}
