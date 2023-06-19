package cinema.dao.impl;

import cinema.dao.CinemaHallDao;
import cinema.dao.MovieDao;
import cinema.dao.MovieSessionDao;
import cinema.dao.ShoppingCartDao;
import cinema.dao.TicketDao;
import cinema.dao.UserDao;
import cinema.exception.DataProcessingException;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    private SessionFactory sessionFactory;

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
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD);
        userDao.add(user);
        Movie mavka = new Movie();
        mavka.setTitle(MOVIE_TITLE);
        movieDao.add(mavka);
        CinemaHall multiplex = new CinemaHall();
        multiplex.setDescription(CINEMA_HALL_DESCR);
        multiplex.setCapacity(CINEMA_HALL_CAPACITY);
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
    void getByUser_getEmptyCart_ok() {
        shoppingCartDao.add(shoppingCart);
        ShoppingCart actual = shoppingCartDao.getByUser(user);
        Assertions.assertTrue(actual.getTickets().isEmpty());
    }

    @Test
    void getByUser_exception_dataProcessingException() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        shoppingCartDao = new ShoppingCartDaoImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenThrow(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class,
                () -> shoppingCartDao.getByUser(user));
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

    @Test
    void update_exception_dataProcessingException() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        shoppingCartDao = new ShoppingCartDaoImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenThrow(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class,
                () -> shoppingCartDao.update(shoppingCart));
    }

    @Test
    void update_transactionFail_dataProcessingException() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        Session session = Mockito.mock(Session.class);
        Transaction transaction = Mockito.mock(Transaction.class);
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
        Mockito.when(session.beginTransaction()).thenReturn(transaction);
        Mockito.doThrow(HibernateException.class).when(session).update(shoppingCart);
        Assertions.assertThrows(DataProcessingException.class,
                () -> shoppingCartDao.update(shoppingCart));
    }
}
