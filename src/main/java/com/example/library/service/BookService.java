package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.exception.BookExistException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.UserAlreadyHaveBook;
import com.example.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    public static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private UserService userService;

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Creating a book.
     * There can't be books with the same title.
     */
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

    /**
     * Updating the book.
     */
    public Book updateBook(Long bookId, BookDTO bookDTO) {
        Book book = getBookById(bookId);
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublishDate(bookDTO.getPublishDate());

        LOG.info("Book: " + book.getTitle() + " has been updated");
        return bookRepository.save(book);
    }

    /**
     * Deleting a book.
     * Before deleting a book, this book is deleted from all users who had it in their library.
     */
    public void deleteBook(Long bookId) {
        Book book = getBookById(bookId);
        List<User> users = userService.getAllUsersForBook(bookId);
        for (User user: users) {
            user.getBooks().remove(book);
            userService.saveUser(user);
        }
        LOG.info("Book: " + getBookById(bookId).getTitle() + " has been deleted");
        bookRepository.delete(getBookById(bookId));
    }

    /**
     * Search for all books belonging to one user.
     */
    public List<Book> getAllBooksForUser(Long userId) {
        User user = userService.getUserById(userId);
        return bookRepository.findAllBooksForUser(user.getId())
                .orElseThrow(() -> new BookNotFoundException("Book not found for userId: " + userId));
    }

    /**
     * Adding a book to a user's library.
     * The user cannot take this book if he already has it in the library.
     */
    public List<Book> takeBookForUser(Long bookId, Long userId) {
        Book book = getBookById(bookId);
        User user = userService.getUserById(userId);

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

    /**
     * Deleting a book from a user's library.
     * The book cannot be deleted if the user does not have it.
     */
    public List<Book> deleteBookForUser(Long bookId, Long userId) {
        Book book = getBookById(bookId);
        User user = userService.getUserById(userId);

        if (user.getBooks().contains(book)) {
            user.getBooks().remove(book);
            bookRepository.save(book);
            return bookRepository.findAllBooksForUser(userId)
                    .orElseThrow(() -> new BookNotFoundException("Book not found for User: " + userId));
        } else {
            throw new UserAlreadyHaveBook("User: " + userId + " already has this book: " + bookId);
        }
    }

    /**
     * Get a book by id.
     * The book cannot be obtained if there is no such id.
     */
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
    }
}
