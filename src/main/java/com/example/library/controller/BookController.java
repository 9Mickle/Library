package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.facade.BookFacade;
import com.example.library.service.BookService;
import com.example.library.validation.MessageResponse;
import com.example.library.validation.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/book")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookFacade bookFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    public ResponseEntity<Object> createNewBook(@Valid @RequestBody BookDTO bookDTO, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Book book = bookService.createBook(bookDTO);
        return new ResponseEntity<>(new MessageResponse("Book created successfully"), HttpStatus.OK);
    }

    @PostMapping("/{bookId}/update")
    public ResponseEntity<Object> updateBook(@Valid @RequestBody BookDTO bookDTO, BindingResult bindingResult,
                                             @PathVariable("bookId") String bookId) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Book book = bookService.updateBook(Long.parseLong(bookId), bookDTO);
        BookDTO bookUpdated = bookFacade.bookToBookDTO(book);
        return new ResponseEntity<>(bookUpdated, HttpStatus.OK);
    }

    @PostMapping("/{bookId}/delete")
    public ResponseEntity<MessageResponse> deleteBook(@Valid @PathVariable("bookId") String bookId) {
        bookService.deleteBook(Long.parseLong(bookId));
        return new ResponseEntity<>(new MessageResponse("Book was deleted"), HttpStatus.OK);

    }

    @PostMapping("/{bookId}/{userId}/add")
    public ResponseEntity<Object> takeBook(@Valid @PathVariable("bookId") String bookId,
                                           @PathVariable("userId") String userId) {

        List<BookDTO> bookDTOList = bookService.takeBookForUser(Long.parseLong(bookId), Long.parseLong(userId))
                .stream()
                .map(bookFacade::bookToBookDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }

    @PostMapping("/{bookId}/{userId}/delete")
    public ResponseEntity<Object> deleteBookForUser(@Valid @PathVariable("bookId") String bookId,
                                                    @PathVariable("userId") String userId) {
        List<BookDTO> bookDTOList = bookService.deleteBookForUser(Long.parseLong(bookId), Long.parseLong(userId))
                .stream()
                .map(bookFacade::bookToBookDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }

    @GetMapping("/{userId}/my_books_all")
    public ResponseEntity<List<BookDTO>> getAllBooksForUser(@PathVariable("userId") String userId) {
        List<BookDTO> bookDTOList = bookService.getAllBooksForUser(Long.parseLong(userId))
                .stream()
                .map(bookFacade::bookToBookDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }
}
