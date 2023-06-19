package cinema.dao.impl;

import cinema.dao.RoleDao;
import cinema.exception.DataProcessingException;
import cinema.model.Role;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleDaoImplTest extends AbstractTest {
    private static RoleDao roleDao;
    private static Role role;
    private SessionFactory sessionFactory;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { Role.class };
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.add(role);
    }

    @Test
    void getRoleByName_existingRoleName_ok() {
        Role actual = roleDao.getByName(Role.RoleName.USER).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_notExistingRoleName_isNotPresent() {
        Optional<Role> actual = roleDao.getByName(Role.RoleName.ADMIN);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, () -> actual.get());
    }

    @Test
    void getByName_exception_dataProcessingException() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        roleDao = new RoleDaoImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenThrow(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.getByName(null));
    }
}
