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
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;
    private User testUser;
    private SessionFactory sessionFactory;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { User.class, Role.class };
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
        Role role = new Role();
        role.setRoleName(RoleName.USER);
        testUser.setRoles(Set.of(roleDao.add(role)));
    }

    @Test
    void findByEmail_existingEmail_ok() {
        userDao.add(testUser);
        User actual = userDao.findByEmail(TEST_EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail());
    }

    @Test
    void findByEmail_notExistingEmail_isNotPresent() {
        Optional<User> actual = userDao.findByEmail(TEST_EMAIL);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, () -> actual.get());
    }

    @Test
    void findByEmail_exception_dataProcessingException() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        userDao = new UserDaoImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenThrow(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.findByEmail(null));
    }
}
