package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.UserAlreadyHaveBook;
import com.example.library.exception.UserExistException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    public static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Book createBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublishDate(bookDTO.getPublishDate());

        try {
            LOG.info("Book saving: {}", book.getTitle());
            return bookRepository.save(book);
        } catch (Exception e) {
            LOG.error("Error saving book {}", e.getMessage());
            throw new UserExistException("The Book: " + book.getTitle() + " already exist. Please check title.");
        }
    }

    public Book updateBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublishDate(bookDTO.getPublishDate());

        LOG.info("Book: " + book.getTitle() + " has been updated");
        return bookRepository.save(book);
    }

    public void deleteBook(Long bookId) {
        LOG.info("Book: " + getBookById(bookId).getTitle() + " has been deleted");
        bookRepository.delete(getBookById(bookId));
    }

    public List<Book> getAllBookForUser(Long userId) {
        return bookRepository.findAllBookForUser(userId)
                .orElseThrow(() -> new BookNotFoundException("Book not found for userId: " + userId));
    }

    public Book takeBookForUser(Long bookId, Long userId) {
        Book book = bookRepository.getById(bookId);
        User user = userRepository.getById(userId);

        if (!book.getUsers().contains(user)) {
            user.getBooks().add(book);
            book.getUsers().add(user);
            bookRepository.save(book);
            userRepository.save(user);
            return book;
        } else {
            throw new UserAlreadyHaveBook("User: " + userId + " already has this book: " + bookId);
        }
    }

    public Book getBookById(Long bookId) {
        return bookRepository.getById(bookId);
    }
}
