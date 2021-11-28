package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.exception.BookExistException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.UserAlreadyHaveBook;
import com.example.library.exception.UserNotFoundException;
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
            throw new BookExistException("The Book: " + book.getTitle() + " already exist. Please check title.");
        }
    }

    public Book updateBook(Long bookId, BookDTO bookDTO) {
        Book book = getBookById(bookId);
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublishDate(bookDTO.getPublishDate());

        LOG.info("Book: " + book.getTitle() + " has been updated");
        return bookRepository.save(book);
    }

    public void deleteBook(Long bookId) {
        Book book = getBookById(bookId);
        List<User> users = userRepository.findAllUsersForBook(bookId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Book: " + bookId));
        for (User user: users) {
            user.getBooks().remove(book);
            userRepository.save(user);
        }
        LOG.info("Book: " + getBookById(bookId).getTitle() + " has been deleted");
        bookRepository.delete(getBookById(bookId));
    }

    public List<Book> getAllBookForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return bookRepository.findAllBooksForUser(user.getId())
                .orElseThrow(() -> new BookNotFoundException("Book not found for userId: " + userId));
    }

    public List<Book> takeBookForUser(Long bookId, Long userId) {
        Book book = getBookById(bookId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (!book.getUsers().contains(user)) {
            user.getBooks().add(book);
            book.getUsers().add(user);
            bookRepository.save(book);
            return bookRepository.findAllBooksForUser(userId)
                    .orElseThrow(() -> new BookNotFoundException("Book not found for User: " + userId));
        } else {
            throw new UserAlreadyHaveBook("User: " + userId + " already has this book: " + bookId);
        }
    }

    public List<Book> deleteBookForUser(Long bookId, Long userId) {
        Book book = getBookById(bookId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (user.getBooks().contains(book)) {
            user.getBooks().remove(book);
            bookRepository.save(book);
            return bookRepository.findAllBooksForUser(userId)
                    .orElseThrow(() -> new BookNotFoundException("Book not found for User: " + userId));
        } else {
            throw new UserAlreadyHaveBook("User: " + userId + " already has this book: " + bookId);
        }
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
    }
}
