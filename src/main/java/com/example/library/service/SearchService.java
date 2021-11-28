package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.exception.BookNotFoundException;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final BookRepository bookRepository;

    public SearchService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findAllByTitleContaining(title)
                .orElseThrow(() -> new BookNotFoundException("Book not found with title: " + title));
    }

    public List<Book> searchByDescription(String description) {
        return bookRepository.findAllByDescriptionContaining(description)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with description: " + description));
    }

    public List<Book> searchByPublishDate(Integer publishDate) {
        return bookRepository.findAllByPublishDate(publishDate)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with publishDate: " + publishDate));
    }

    public List<Book> searchByTitleAndDescription(String title, String description) {
        return bookRepository.findAllByTitleContainingAndDescriptionContaining(title, description)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with title: " + title + " and description: " + description));
    }

    public List<Book> searchByTitleAndDate(String title, Integer date) {
        return bookRepository.findAllByTitleContainingAndPublishDate(title, date)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with title: " + title + " and date: " + date));
    }

    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }
}
