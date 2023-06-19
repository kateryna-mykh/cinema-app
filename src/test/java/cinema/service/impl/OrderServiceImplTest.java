package cinema.service.impl;

import cinema.dao.OrderDao;
import cinema.model.Order;
import cinema.model.Role;
import cinema.model.ShoppingCart;
import cinema.model.User;
import cinema.model.Role.RoleName;
import cinema.service.OrderService;
import cinema.service.ShoppingCartService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderServiceImplTest {
    private static final String EMAIL = "test-mail@i.ua";
    private static final String PASSWORD = "1234";
    private static final RoleName ROLE = Role.RoleName.USER;
    private static OrderService orderService;
    private static OrderDao orderDao;
    private static ShoppingCartService shoppingCartService;
    private static ShoppingCart shoppingCart;
    private static User user;
    private static Order order;

    @BeforeAll
    static void setUpBeforeClass() {
        orderDao = Mockito.mock(OrderDao.class);
        shoppingCartService = Mockito.mock(ShoppingCartService.class);
        orderService = new OrderServiceImpl(orderDao, shoppingCartService);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(ROLE)));
        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        order = new Order();
        order.setUser(user);
        order.setOrderTime(LocalDateTime.of(2023, 6, 16, 18, 0));
    }

    @Test
    void completeOrder_ok() {
        Mockito.when(orderDao.add(order)).thenReturn(order);
        Mockito.doNothing().when(shoppingCartService).clear(shoppingCart);
        Order actual = orderService.completeOrder(shoppingCart);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(order.getUser(), actual.getUser());
    }

    @Test
    void getOrdersHistory_ok() {
        Mockito.when(orderDao.getOrdersHistory(user)).thenReturn(List.of(order));
        List<Order> actual = orderService.getOrdersHistory(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(List.of(order), actual);
    }
}
