package com.cinquecento.simpleserverapi.rest;

import com.cinquecento.simpleserverapi.dto.UserDTO;
import com.cinquecento.simpleserverapi.model.Status;
import com.cinquecento.simpleserverapi.model.User;
import com.cinquecento.simpleserverapi.service.impl.UserServiceImpl;
import com.cinquecento.simpleserverapi.util.ErrorMessageBuilder;
import com.cinquecento.simpleserverapi.util.UserConverter;
import com.cinquecento.simpleserverapi.util.exception.*;
import com.cinquecento.simpleserverapi.util.response.UserErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
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
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<UserDTO> get(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        UserDTO user = userConverter.convertToUserDTO(userService.findById(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Order
    public ResponseEntity<Map<String, Long>> create(@RequestBody @Valid UserDTO userDTO,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedException(errorMessageBuilder.message(bindingResult));
        }

        Long id = userService.save(userConverter.convertToUser(userDTO));
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("ID", id));
    }

    @PatchMapping(value = "/change-status/{id}/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Order
    public ResponseEntity<Map<Long, List<String>>> changeStatus(@PathVariable(name = "id") Long id,
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

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of(id, List.of(currentStatus.toString(), status)));
    }

    /*
        So far this method seems too messy, I need to think about refactoring it
     */
    @GetMapping(value = "/statistic-to-repair", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<UserDTO>>> statistic(@RequestParam String status,
                                                   @RequestParam String fieldForSort,
                                                   @RequestParam String order) {

        if (
                status.equalsIgnoreCase(Status.ONLINE.toString()) ||
                status.equalsIgnoreCase(Status.OFFLINE.toString()) ||
                status.equalsIgnoreCase(Status.ABSENT.toString())
        ) {
            throw new IllegalStatusException("Irrelevant status: " + status);
        } else if (
                order.equalsIgnoreCase("asc") ||
                order.equalsIgnoreCase("desc")
        ) {
            throw new IllegalSortingOrderException("Irrelevant sorting order: " + order);
        } else if (Arrays.stream(User.class.getDeclaredFields())
                .anyMatch(field -> field.toString().equalsIgnoreCase(fieldForSort))) {
            throw new IllegalFieldForSortException("Irrelevant field: " + fieldForSort);
        }

        List<UserDTO> users = userService
                .findByStatus(Status.valueOf(status), fieldForSort, order)
                .stream()
                .map(userConverter::convertToUserDTO)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of(new Date().toString(), users));
    }

    @GetMapping("/statistic")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<Map<String, List<UserDTO>>> statistic(@RequestParam String ID,
                                                                @RequestParam String status) {

        LocalDateTime dateTimeAfter;
        Status parsedStatus;

        try {
            dateTimeAfter = LocalDateTime.parse(ID);
            parsedStatus = Status.valueOf(status.toUpperCase());
        } catch (DateTimeParseException e) {
            throw new IllegalTimestampInQueryException("Irrelevant ID timestamp: " + ID);
        } catch (IllegalArgumentException e) {
            throw new IllegalStatusException("Irrelevant status: " + status);
        }

        List<UserDTO> users = userService
                .findByStatus(parsedStatus, dateTimeAfter)
                    .stream()
                    .map(userConverter::convertToUserDTO).toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of(ID, users));
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

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(IllegalStatusException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(IllegalSortingOrderException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(IllegalFieldForSortException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(IllegalTimestampInQueryException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
