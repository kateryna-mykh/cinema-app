package cinema.dao.impl;

import cinema.dao.CinemaHallDao;
import cinema.dao.MovieDao;
import cinema.dao.MovieSessionDao;
import cinema.dao.TicketDao;
import cinema.dao.UserDao;
import cinema.model.Ticket;
import cinema.model.User;
import java.time.LocalDateTime;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketDaoImplTest extends AbstractTest {
    private TicketDao ticketDao;
    private UserDao userDao;
    private MovieSessionDao movieSessionDao;
    private CinemaHallDao cinemaHallDao;
    private MovieDao movieDao;
    private Ticket ticket;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { Ticket.class, User.class, MovieSession.class, CinemaHall.class, Movie.class, Role.class };
    }

    @BeforeEach
    void setUp() {
        ticketDao = new TicketDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        movieSessionDao = new MovieSessionDaoImpl(getSessionFactory());
        cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        movieDao = new MovieDaoImpl(getSessionFactory());

        User user = new User();
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
    }

    @Test
    void add_ok() {
        Ticket actual = ticketDao.add(ticket);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }
}
