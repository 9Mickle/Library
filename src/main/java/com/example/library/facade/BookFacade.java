package com.example.library.facade;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookFacade {

    public BookDTO bookToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setPublishDate(book.getPublishDate());
        return bookDTO;
    }
}
