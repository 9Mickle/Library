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

    /**
     * Search for a book by title.
     * The book cannot be found if there is no such title.
     */
    public List<Book> searchByTitle(String title) {
        return bookRepository.findAllByTitleContaining(title)
                .orElseThrow(() -> new BookNotFoundException("Book not found with title: " + title));
    }

    /**
     * Search for a book by description.
     * The book cannot be found if there is no such description.
     */
    public List<Book> searchByDescription(String description) {
        return bookRepository.findAllByDescriptionContaining(description)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with description: " + description));
    }

    /**
     * Search for a book by publish date.
     * The book cannot be found if there is no such publish date.
     */
    public List<Book> searchByPublishDate(Integer publishDate) {
        return bookRepository.findAllByPublishDate(publishDate)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with publishDate: " + publishDate));
    }

    /**
     * Search for a book by title and description.
     * The book cannot be found if there is not at least a partial match in the title and description
     */
    public List<Book> searchByTitleAndDescription(String title, String description) {
        return bookRepository.findAllByTitleContainingAndDescriptionContaining(title, description)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with title: " + title + " and description: " + description));
    }

    /**
     * Search for a book by title and publish date.
     * The book cannot be found if there is not at least a partial match in the title and date
     */
    public List<Book> searchByTitleAndDate(String title, Integer date) {
        return bookRepository.findAllByTitleContainingAndPublishDate(title, date)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with title: " + title + " and date: " + date));
    }

    /**
     * Get all the books.
     * If there are no books, an empty list will be returned.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
