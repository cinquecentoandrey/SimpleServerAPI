package com.cinquecento.simpleserverapi.rest;

import com.cinquecento.simpleserverapi.service.impl.UserServiceImpl;
import com.cinquecento.simpleserverapi.util.ErrorMessageBuilder;
import com.cinquecento.simpleserverapi.util.UserConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserServiceImpl userService;

    @Mock
    UserConverter converter;

    @Mock
    ErrorMessageBuilder messageBuilder;

    @InjectMocks
    UserController userController;

    @Test
    void get_ReturnsUserDTO() {

    }
}