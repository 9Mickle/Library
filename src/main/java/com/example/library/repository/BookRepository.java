package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findById(Long bookId);

    Optional<List<Book>> findAllByDescriptionContaining(String description);

    Optional<List<Book>> findAllByTitleContaining(String title);

    Optional<List<Book>> findAllByPublishDate(Integer publishDate);

    Optional<List<Book>> findAllByTitleContainingAndDescriptionContaining(String title, String description);

    Optional<List<Book>> findAllByTitleContainingAndPublishDate(String title, Integer date);

    @Query(nativeQuery = true, value = "Select b.* from user_book ub Left join book b on b.Id = ub.book_id Where ub.user_id = (:userId)")
    Optional<List<Book>> findAllBookForUser(@Param("userId") Long userId);
}
