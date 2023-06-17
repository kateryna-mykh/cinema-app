package cinema.service.impl;

import cinema.dao.UserDao;
import cinema.model.User;
import cinema.model.Role;
import cinema.service.UserService;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String VALID_EMAIL = "test-mail@i.ua";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 0L;
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static UserService userService;
    private static User testUser;

    @BeforeAll
    static void setUpBeforeClass() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(passwordEncoder, userDao);
        testUser = new User();
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(PASSWORD);
        testUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        testUser.setId(VALID_ID);
    }

    @Test
    void add_ok() {
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        Mockito.when(userDao.add(testUser)).thenReturn(testUser);
        User actual = userService.add(testUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertEquals(testUser, actual);
    }

    @Test
    void get_existId_ok() {
        Mockito.when(userDao.get(VALID_ID)).thenReturn(Optional.of(testUser));
        User actual = userService.get(VALID_ID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail());
        Assertions.assertEquals(testUser.getPassword(), actual.getPassword());
    }

    @Test
    void get_notExistId_runtimeException() {
        Mockito.when(userDao.get(VALID_ID)).thenReturn(Optional.of(testUser));
        Assertions.assertThrows(RuntimeException.class,
                () -> userService.get(INVALID_ID));
    }

    @Test
    void findByEmail_existEmail_ok() {
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));
        Optional<User> actual = userService.findByEmail(VALID_EMAIL);
        Assertions.assertEquals(Optional.of(testUser), actual);
    }

    @Test
    void findByEmail_notExistEmail_emptyOptional() {
        Mockito.when(userDao.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(INVALID_EMAIL);
        Assertions.assertEquals(Optional.empty(), actual);
    }
}
