package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.facade.BookFacade;
import com.example.library.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/search")
@CrossOrigin
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private BookFacade bookFacade;

    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> getALlBooks() {
        List<BookDTO> bookDTOList = searchService.getAllBook()
                .stream()
                .map(bookFacade::bookToBookDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }

    @GetMapping("title/{bookTitle}")
    public ResponseEntity<List<BookDTO>> getBookByTitle(@PathVariable("bookTitle") String bookTitle) {
        List<BookDTO> books = searchService.searchByTitle(bookTitle)
                .stream()
                .map(bookFacade::bookToBookDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("description/{bookDescription}")
    public ResponseEntity<List<BookDTO>> getBookByDescription(@PathVariable("bookDescription") String bookDescription) {
        List<BookDTO> books = searchService.searchByDescription(bookDescription)
                .stream()
                .map(bookFacade::bookToBookDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("date/{publishDate}")
    public ResponseEntity<List<BookDTO>> getBooksByPublishDate(@PathVariable("publishDate") Integer publishDate) {
        List<BookDTO> bookDTOList = searchService.searchByPublishDate(publishDate)
                .stream()
                .map(bookFacade::bookToBookDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }

    @GetMapping("title/{title}/description/{description}")
    public ResponseEntity<List<BookDTO>> getBooksByTitleAndDescription(@PathVariable("title") String title,
                                                               @PathVariable("description") String description) {
        List<BookDTO> bookDTOList = searchService.searchByTitleAndDescription(title, description)
                .stream()
                .map(bookFacade::bookToBookDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }

    @GetMapping("title/{title}/date/{date}")
    public ResponseEntity<List<BookDTO>> getBooksByTitleAndDescription(@PathVariable("title") String title,
                                                                       @PathVariable("date") Integer date) {
        List<BookDTO> bookDTOList = searchService.searchByTitleAndDate(title, date)
                .stream()
                .map(bookFacade::bookToBookDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }
}
