package com.library.api.services.impl;

import com.library.api.domain.Book;
import com.library.api.exceptions.BussinesException;
import com.library.api.repositories.BookRepository;
import com.library.api.services.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
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
       repository.deleteById(id);
    }

    @Override
    public Book update(Book book) {
        return repository.save(book);
    }

}
