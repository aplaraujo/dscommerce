package com.example.dscommerce.services;

import com.example.dscommerce.entities.User;
import com.example.dscommerce.services.exceptions.ForbiddenException;
import com.example.dscommerce.tests.UserDetailsFactory;
import com.example.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    private User userAdmin;
    private User selfClient;
    private User otherClient;

    @BeforeEach
    void setUp() throws Exception {
        userAdmin = UserFactory.createAdminUser();
        selfClient = UserFactory.createCustomClientUser(1L, "Bob");
        otherClient = UserFactory.createCustomClientUser(2L, "Ana");
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenAdminIsLogged() {
        when(userService.authenticated()).thenReturn(userAdmin);
        Long userId = userAdmin.getId();

        Assertions.assertDoesNotThrow(() -> {
            authService.validateSelfOrAdmin(userId);
        });
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenSelfIsLogged() {
        when(userService.authenticated()).thenReturn(selfClient);
        Long userId = selfClient.getId();

        Assertions.assertDoesNotThrow(() -> {
            authService.validateSelfOrAdmin(userId);
        });
    }

    @Test
    public void validateSelfOrAdminThrowsForbiddenExceptionWhenOtherClientIsLogged() {
        when(userService.authenticated()).thenReturn(selfClient);
        Long userId = otherClient.getId();

        Assertions.assertThrows(ForbiddenException.class, () -> {
            authService.validateSelfOrAdmin(userId);
        });
    }
}
