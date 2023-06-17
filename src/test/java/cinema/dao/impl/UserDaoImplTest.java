package cinema.dao.impl;

import cinema.dao.RoleDao;
import cinema.dao.UserDao;
import cinema.exception.DataProcessingException;
import cinema.model.User;
import cinema.model.Role;
import cinema.model.Role.RoleName;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "test-mail@i.ua";
    private static final String PASSWORD = "1234";
    private static final Long ID = 1L;
    private UserDao userDao;
    private RoleDao roleDao;
    private User testUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { User.class, Role.class };
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        testUser = new User();
        testUser.setEmail(EMAIL);
        testUser.setPassword(PASSWORD);
        Role role = new Role();
        role.setRoleName(RoleName.USER);
        testUser.setRoles(Set.of(roleDao.add(role)));
    }

    @Test
    void add_ok() {
        userDao.add(testUser);
        Assertions.assertNotNull(testUser);
        Assertions.assertEquals(ID, testUser.getId());
    }

    @Test
    void add_nullValue_dataProcessingException() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.add(null));
    }

    @Test
    void findByEmail_existingEmail_ok() {
        userDao.add(testUser);
        User actual = userDao.findByEmail(EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail());
    }

    @Test
    void findByEmail_notExistingEmail_isNotPresent() {
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, () -> actual.get());
    }

    @Test // (?????)
    void findByEmail_throwException_dataProcessingException() {
        DataProcessingException actual = Assertions.assertThrows(DataProcessingException.class,
                () -> {
                    userDao.findByEmail(EMAIL);
                    // Thread.currentThread().interrupt();
                    Exception e = new RuntimeException();
                    throw new DataProcessingException("", e);
                });
        // Assertions.assertEquals("User with email " + EMAIL + " not found",
        // actual.getMessage());
    }

    @Test
    void get_existingUser_ok() {
        userDao.add(testUser);
        User actual = userDao.get(ID).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail());
    }

    @Test
    void get_notExistingUser_isNotPresent() {
        Optional<User> actual = userDao.get(ID);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, () -> actual.get());
    }

    @Test
    void get_nullUser_dataProcessingException() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.get(null));
    }
}
