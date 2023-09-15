package com.cinquecento.simpleserverapi.rest;

import com.cinquecento.simpleserverapi.dto.UserDTO;
import com.cinquecento.simpleserverapi.model.Status;
import com.cinquecento.simpleserverapi.model.User;
import com.cinquecento.simpleserverapi.service.impl.UserServiceImpl;
import com.cinquecento.simpleserverapi.util.ErrorMessageBuilder;
import com.cinquecento.simpleserverapi.util.UserConverter;
import com.cinquecento.simpleserverapi.util.exception.IncorrectStatusException;
import com.cinquecento.simpleserverapi.util.exception.UserNotCreatedException;
import com.cinquecento.simpleserverapi.util.exception.UserNotFoundException;
import com.cinquecento.simpleserverapi.util.response.UserErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;
    private final UserConverter userConverter;
    private final ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    public UserController(UserServiceImpl userService, UserConverter userConverter,
                          ErrorMessageBuilder errorMessageBuilder) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO get(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        return userConverter.convertToUserDTO(userService.findById(id));
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> create(@RequestBody @Valid UserDTO userDTO,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedException(errorMessageBuilder.message(bindingResult));
        }

        return Map.of("ID", userService.save(userConverter.convertToUser(userDTO)));
    }

    @PatchMapping(value = "/change-status/{id}/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<Long, List<String>> changeStatus(@PathVariable(name = "id") Long id,
                                                @PathVariable(name = "status") String status) throws UserNotFoundException{
        User user = userService.findById(id);
        Status currentStatus = user.getStatus();

        if (status.equalsIgnoreCase(Status.ONLINE.toString())) {
            userService.updateStatus(user, Status.ONLINE);
        } else if (status.equalsIgnoreCase(Status.OFFLINE.toString())) {
            userService.updateStatus(user, Status.OFFLINE);
        } else {
            throw new IncorrectStatusException("Incorrect status: " + status);
        }

        return Map.of(id, List.of(currentStatus.toString(), status));
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(UserNotFoundException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(UserNotCreatedException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(IncorrectStatusException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
