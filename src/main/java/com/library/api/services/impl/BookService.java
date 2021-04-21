package com.library.api.services.impl;

import com.library.api.domain.Book;
import com.library.api.exceptions.BussinesException;
import com.library.api.repositories.BookRepository;
import com.library.api.services.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class BookService implements IBookService {

    private BookRepository repository;

    public BookService(BookRepository repo) {
        this.repository = repo;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw new BussinesException("IBSN ja existente!");
        }
        return repository.save(book);

    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) throws ResponseStatusException {
        if(id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

       repository.deleteById(id);
    }

    @Override
    public Book update(Book book) {
        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return repository.save(book);
    }

    @Override
    public Page<Book> find(Book filter, Pageable pageRequest) {
        Example<Book> example = Example.of(filter, ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withIgnoreNullValues()
        .withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example, pageRequest);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return repository.findByIsbn(isbn);
    }

}
