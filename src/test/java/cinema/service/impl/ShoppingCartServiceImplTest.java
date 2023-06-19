package cinema.service.impl;

import cinema.dao.ShoppingCartDao;
import cinema.dao.TicketDao;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.model.Role;
import cinema.model.User;
import cinema.model.Role.RoleName;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.service.ShoppingCartService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ShoppingCartServiceImplTest {
    private static final String EMAIL = "test-mail@i.ua";
    private static final String PASSWORD = "1234";
    private static final RoleName ROLE = Role.RoleName.USER;
    private static ShoppingCartService shoppingCartService;
    private static ShoppingCartDao shoppingCartDao;
    private static TicketDao ticketDao;
    private static User user;
    private static ShoppingCart shoppingCart;
    private static Ticket ticket;
    private static MovieSession movieSession;

    @BeforeAll
    static void setUpBeforeClass() {
        shoppingCartDao = Mockito.mock(ShoppingCartDao.class);
        ticketDao = Mockito.mock(TicketDao.class);
        shoppingCartService = new ShoppingCartServiceImpl(shoppingCartDao, ticketDao);
        user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(ROLE)));
        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(1L);
        Movie mavka = new Movie();
        mavka.setTitle("Mavka. The Forest Song");
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(150);
        cinemaHall.setDescription("Multiplex");
        movieSession = new MovieSession();
        movieSession.setMovie(mavka);
        movieSession.setCinemaHall(cinemaHall);
        ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        ticket.setId(1L);
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        shoppingCart.setTickets(tickets);
    }

    @Test
    void addSession_ok() {
        shoppingCart = Mockito.mock(ShoppingCart.class);
        Mockito.when(shoppingCartDao.getByUser(user)).thenReturn(shoppingCart);
        Mockito.when(shoppingCartDao.update(shoppingCart)).thenReturn(shoppingCart);
        Mockito.when(ticketDao.add(ticket)).thenReturn(ticket);
        List<Ticket> tickets = new ArrayList<>();
        Mockito.when(shoppingCart.getTickets()).thenReturn(tickets);
        shoppingCartService.addSession(movieSession, user);
        Assertions.assertNotNull(shoppingCart.getTickets());
    }

    @Test
    void getByUser_ok() {
        Mockito.when(shoppingCartDao.getByUser(user)).thenReturn(shoppingCart);
        ShoppingCart actual = shoppingCartService.getByUser(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
    }

    @Test
    void registerNewShoppingCart_ok() {
        Mockito.when(shoppingCartDao.add(shoppingCart)).thenReturn(shoppingCart);
        shoppingCartService.registerNewShoppingCart(user);
        Assertions.assertEquals(user.getId(), shoppingCart.getId());
    }

    @Test
    void clear_ok() {
        List<Ticket> beforeClear = shoppingCart.getTickets();
        Assertions.assertNotNull(beforeClear);
        Mockito.when(shoppingCartDao.update(shoppingCart)).thenReturn(shoppingCart);
        shoppingCartService.clear(shoppingCart);
        List<Ticket> afterClear = shoppingCart.getTickets();
        Assertions.assertNull(afterClear);
    }
}
