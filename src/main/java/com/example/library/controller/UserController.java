package com.example.library.controller;

import com.example.library.dto.UserDTO;
import com.example.library.entity.User;
import com.example.library.facade.UserFacade;
import com.example.library.service.UserService;
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
@RequestMapping("api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    public ResponseEntity<Object> createNewUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        userService.createUser(userDTO);
        return ResponseEntity.ok(new MessageResponse("User created successfully"));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        User user = userService.updateUser(userDTO);
        UserDTO userUpdated = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

    @PostMapping("/{userId}/delete")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(Long.parseLong(userId));
        return new ResponseEntity<>(new MessageResponse("User was deleted"), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUser() {
        List<UserDTO> userDTOList = userService.getAllUser()
                .stream()
                .map(userFacade::userToUserDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }
}
