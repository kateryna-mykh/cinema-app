package cinema.service.impl;

import cinema.model.Role;
import cinema.model.Role.RoleName;
import cinema.model.User;
import cinema.service.AuthenticationService;
import cinema.service.RoleService;
import cinema.service.ShoppingCartService;
import cinema.service.UserService;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "test-mail@i.ua";
    private static final String PASSWORD = "1234";
    private static final RoleName ROLE = Role.RoleName.USER;
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static ShoppingCartService shoppingCartService;
    private static RoleService roleService;

    @BeforeAll
    static void setUpBeforeClass() {
        userService = Mockito.mock(UserService.class);
        shoppingCartService = Mockito.mock(ShoppingCartService.class);
        roleService = Mockito.mock(RoleService.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                shoppingCartService);
    }

    @Test
    void register_ok() {
        User testUser = new User();
        testUser.setEmail(EMAIL);
        testUser.setPassword(PASSWORD);
        Mockito.when(roleService.getByName(ROLE)).thenReturn(new Role(RoleName.USER));
        testUser.setRoles(Set.of(roleService.getByName(ROLE)));
        Mockito.when(userService.add(testUser)).thenReturn(testUser);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser, actual);
    }
}
