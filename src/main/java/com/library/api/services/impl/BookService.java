package com.library.api.services.impl;

import com.library.api.domain.Book;
import com.library.api.repositories.BookRepository;
import com.library.api.services.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService implements IBookService {

    private BookRepository repository;

    public BookService(BookRepository repo) {
        this.repository = repo;
    }

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }
}
