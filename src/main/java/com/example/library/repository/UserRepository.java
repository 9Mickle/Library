package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long userId);

    /**
     * Search for all users who have this book.
     */
    @Query(nativeQuery = true, value = "Select u.* from user_book ub Right join user u on u.Id = ub.user_id Where ub.book_id = (:bookId)")
    Optional<List<User>> findAllUsersForBook(@Param("bookId") Long bookId);
}
