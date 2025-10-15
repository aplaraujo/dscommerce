package com.example.dscommerce.services;

import com.example.dscommerce.dto.UserDTO;
import com.example.dscommerce.entities.User;
import com.example.dscommerce.projections.UserDetailsProjection;
import com.example.dscommerce.repositories.UserRepository;
import com.example.dscommerce.tests.UserDetailsFactory;
import com.example.dscommerce.tests.UserFactory;
import com.example.dscommerce.util.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomUserUtil customUserUtil;

    private String existingUserName;
    private String notExistingUserName;
    private User user;
    private List<UserDetailsProjection> userDetails;

    @BeforeEach
    void setUp() throws Exception {
        existingUserName = "maria@gmail.com";
        notExistingUserName = "user@gmail.com";
        user = UserFactory.createCustomClientUser(1L, existingUserName);
        userDetails = UserDetailsFactory.createCustomAdminUser(existingUserName);

        when(userRepository.searchUserAndRolesByEmail(existingUserName)).thenReturn(userDetails);
        when(userRepository.searchUserAndRolesByEmail(notExistingUserName)).thenReturn(new ArrayList<>());
        when(userRepository.findByEmail(existingUserName)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(notExistingUserName)).thenReturn(Optional.empty());
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
        UserDetails result = userService.loadUserByUsername(existingUserName);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUserName, result.getUsername());

    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(notExistingUserName);
        });
    }

    @Test
    public void authenticatedShouldReturnUserWhenUserExists() {
        when(customUserUtil.getLoggedUsername()).thenReturn(existingUserName);
        User result = userService.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUserName, result.getUsername());
    }

    @Test
    public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        doThrow(ClassCastException.class).when(customUserUtil).getLoggedUsername();
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.authenticated();
        });
    }

    @Test
    public void getMeShouldReturnUserDTOWhenUserIsAuthenticated() {

        UserService spyUserService = spy(userService);
        doReturn(user).when(spyUserService).authenticated();
        UserDTO result = spyUserService.getMe();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUserName, result.getEmail());
    }

    @Test
    public void getMeShouldThrowUsernameNotFoundExceptionWhenUserIsNotAuthenticated() {
        UserService spyUserService = spy(userService);
        doThrow(UsernameNotFoundException.class).when(spyUserService).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDTO result = spyUserService.getMe();
        });
    }
}
