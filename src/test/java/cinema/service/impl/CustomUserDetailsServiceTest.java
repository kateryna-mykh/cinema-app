package cinema.service.impl;

import java.util.Set;
import cinema.model.User;
import cinema.model.Role;
import cinema.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private static CustomUserDetailsService userDetailsService;
    private static UserService userService;
    private static User testUser;

    @BeforeAll
    static void setUpBeforeClass() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        testUser = new User();
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(PASSWORD);
        testUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));
    }

    @Test
    void loadUserByUsername_existingUser_ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(testUser.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getUsername());
        Assertions.assertEquals(testUser.getPassword(), actual.getPassword());
    }

    @Test
    void loadUserByUsername_usernameNotFound_usernameNotFoundException() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(INVALID_EMAIL));
    }
}
