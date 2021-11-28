package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findById(Long bookId);

    /**
     * Partial book search by description.
     */
    Optional<List<Book>> findAllByDescriptionContaining(String description);

    /**
     * Partial book search by title.
     */
    Optional<List<Book>> findAllByTitleContaining(String title);

    /**
     * Partial book search by publish date.
     */
    Optional<List<Book>> findAllByPublishDate(Integer publishDate);

    /**
     * Partial book search by title and description.
     */
    Optional<List<Book>> findAllByTitleContainingAndDescriptionContaining(String title, String description);

    /**
     * Partial book search by title and publish date.
     */
    Optional<List<Book>> findAllByTitleContainingAndPublishDate(String title, Integer date);

    /**
     * Search for all books belonging to one user.
     */
    @Query(nativeQuery = true, value = "Select b.* from user_book ub Left join book b on b.Id = ub.book_id Where ub.user_id = (:userId)")
    Optional<List<Book>> findAllBooksForUser(@Param("userId") Long userId);
}
