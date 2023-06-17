package cinema.dao.impl;

import cinema.dao.RoleDao;
import cinema.model.Role;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static RoleDao roleDao;
    private static Role role;

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
    void add_ok() {
        Assertions.assertNotNull(role);
        Assertions.assertEquals(1L, role.getId());
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
        Assertions.assertThrows(NoSuchElementException.class, 
                () -> actual.get());
    }
}
