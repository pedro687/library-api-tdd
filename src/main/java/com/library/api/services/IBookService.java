package com.library.api.services;

import com.library.api.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IBookService {
    Book save(Book any);
    Optional<Book> findById(Long id);
    void deleteById(Long id);
    Book update(Book book);
    Page<Book> find(Book filter, Pageable pageRequest);
    Optional<Book> findByIsbn(String isbn);
}
