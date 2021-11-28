package com.example.library.service;

import com.example.library.dto.UserDTO;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.exception.UserExistException;
import com.example.library.exception.UserNotFoundException;
import com.example.library.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creating a user.
     * There can't be users with the same email.
     */
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPatronymic(userDTO.getPatronymic());

        try {
            LOG.info("Saving User {}", userDTO.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error saving user {}", e.getMessage());
            throw new UserExistException("The User: " + user.getEmail() + " already exist. Please check email.");
        }
    }

    /**
     * User update.
     */
    public User updateUser(Long userId, UserDTO userDTO) {
        User user = getUserById(userId);
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPatronymic(userDTO.getPatronymic());

        LOG.info("User: " + user.getEmail() + " has been updated");
        return userRepository.save(user);
    }

    /**
     * Deleting a user.
     * The user cannot be deleted if there is no user.
     */
    public void deleteUser(Long userId) {
        LOG.info("User: " + getUserById(userId).getEmail() + " has been deleted");
        userRepository.delete(getUserById(userId));
    }

    /**
     * Saving a user.
     */
    public void saveUser(User user) {
        LOG.info("User: " + user.getEmail() + " has been saved");
        userRepository.save(user);
    }

    /**
     * Getting a user by id.
     * User cannot be obtained if there is no such id.
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    /**
     * Get all users who have this book.
     */
    public List<User> getAllUsersForBook(Long bookId) {
        return userRepository.findAllUsersForBook(bookId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Book: " + bookId));
    }

    /**
     * Get all users.
     * If there are no users, an empty list will be returned.
     */
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
