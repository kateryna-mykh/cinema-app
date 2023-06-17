package cinema.dao.impl;

import cinema.dao.CinemaHallDao;
import cinema.dao.MovieDao;
import cinema.dao.MovieSessionDao;
import cinema.dao.ShoppingCartDao;
import cinema.dao.TicketDao;
import cinema.dao.UserDao;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.model.Role;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingCartDaoImplTest extends AbstractTest {
    private ShoppingCartDao shoppingCartDao;
    private TicketDao ticketDao;
    private UserDao userDao;
    private MovieSessionDao movieSessionDao;
    private CinemaHallDao cinemaHallDao;
    private MovieDao movieDao;
    private ShoppingCart shoppingCart;
    private User user;
    private Ticket ticket;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { ShoppingCart.class, User.class, Role.class, Ticket.class,
                MovieSession.class, Movie.class, CinemaHall.class };
    }

    @BeforeEach
    void setUp() {
        shoppingCartDao = new ShoppingCartDaoImpl(getSessionFactory());
        ticketDao = new TicketDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        movieSessionDao = new MovieSessionDaoImpl(getSessionFactory());
        cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        movieDao = new MovieDaoImpl(getSessionFactory());

        user = new User();
        user.setEmail("test-mail@i.ua");
        user.setPassword("1234");
        userDao.add(user);
        Movie mavka = new Movie();
        mavka.setTitle("Mavka. The Forest Song");
        movieDao.add(mavka);
        CinemaHall multiplex = new CinemaHall();
        multiplex.setDescription("Multiplex");
        multiplex.setCapacity(150);
        cinemaHallDao.add(multiplex);
        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(mavka);
        movieSession.setCinemaHall(multiplex);
        movieSession.setShowTime(LocalDateTime.now());
        movieSessionDao.add(movieSession);
        ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);

        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
    }

    @Test
    void add_ok() {
        ShoppingCart actual = shoppingCartDao.add(shoppingCart);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), shoppingCart.getId());
    }

    @Test
    void getByUser_getEmptyCart_ok() {
        shoppingCartDao.add(shoppingCart);
        ShoppingCart actual = shoppingCartDao.getByUser(user);
        Assertions.assertTrue(actual.getTickets().isEmpty());
    }

    @Test
    void update_addingTicket_ok() {
        String emptyCart = shoppingCartDao.add(shoppingCart).toString();
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        shoppingCart.setTickets(tickets);
        String cartWithTicket = shoppingCartDao.update(shoppingCart).toString();
        Assertions.assertNotEquals(emptyCart, cartWithTicket);
    }
}
