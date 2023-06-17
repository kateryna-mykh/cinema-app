package cinema.service.impl;

import cinema.dao.RoleDao;
import cinema.model.Role;
import cinema.model.Role.RoleName;
import cinema.service.RoleService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final RoleName USER = Role.RoleName.USER;
    private static RoleService roleService;
    private static RoleDao roleDao;
    private static Role role;

    @BeforeAll
    static void setUpBeforeClass() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(USER);
    }

    @Test
    void add_ok() {
        Mockito.when(roleDao.add(role)).thenReturn(role);
        Role actual = roleService.add(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getByName_existName_ok() {
        Mockito.when(roleDao.getByName(USER)).thenReturn(Optional.of(role));
        Role actual = roleService.getByName(USER);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER, actual.getRoleName());
    }

    @Test
    void getByName_notExistName_runtimeException() {
        Mockito.when(roleDao.getByName(USER)).thenReturn(Optional.of(role));
        Assertions.assertThrows(RuntimeException.class,
                () -> roleService.getByName(Role.RoleName.ADMIN));
    }
}
