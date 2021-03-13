package com.library.api.services;

import com.library.api.domain.Book;

import java.util.Optional;

public interface IBookService {
    Book save(Book any);
    Optional<Book> findById(Long id);
    void deleteById(Long id);
    Book update(Book book);
}
