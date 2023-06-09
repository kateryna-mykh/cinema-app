package cinema.dao.impl;

import cinema.dao.CinemaHallDao;
import cinema.dao.MovieDao;
import cinema.dao.MovieSessionDao;
import cinema.dao.OrderDao;
import cinema.dao.RoleDao;
import cinema.dao.TicketDao;
import cinema.dao.UserDao;
import cinema.exception.DataProcessingException;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.model.Order;
import cinema.model.Role;
import cinema.model.Ticket;
import cinema.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderDaoImplTest extends AbstractTest {
    private OrderDao orderDao;
    private UserDao userDao;
    private TicketDao ticketDao;
    private MovieSessionDao movieSessionDao;
    private CinemaHallDao cinemaHallDao;
    private MovieDao movieDao;
    private RoleDao roleDao;
    private Order order;
    private User user;
    private SessionFactory sessionFactory;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { Order.class, Ticket.class, User.class, MovieSession.class,
                CinemaHall.class, Movie.class, Role.class };
    }

    @BeforeEach
    void setUp() {
        orderDao = new OrderDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        ticketDao = new TicketDaoImpl(getSessionFactory());
        movieSessionDao = new MovieSessionDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        movieDao = new MovieDaoImpl(getSessionFactory());

        order = new Order();
        order.setOrderTime(LocalDateTime.now());
        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD);
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.add(role);
        user.setRoles(Set.of(role));
        userDao.add(user);
        order.setUser(user);

        Movie mavka = new Movie();
        mavka.setTitle(MOVIE_TITLE);
        movieDao.add(mavka);
        CinemaHall multiplex = new CinemaHall();
        multiplex.setDescription(CINEMA_HALL_DESCR);
        multiplex.setCapacity(150);
        cinemaHallDao.add(multiplex);
        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(mavka);
        movieSession.setCinemaHall(multiplex);
        movieSession.setShowTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0)));
        movieSessionDao.add(movieSession);
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);
        ticketDao.add(ticket);

        order.setTickets(List.of(ticket));
    }

    @Test
    void getOrdersHistory_ok() {
        orderDao.add(order);
        List<Order> actual = orderDao.getOrdersHistory(user);
        Assertions.assertEquals(List.of(order).size(), actual.size());
    }

    @Test
    void getOrdersHistory_exception_dataProcessingException() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        orderDao = new OrderDaoImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenThrow(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class,
                () -> orderDao.getOrdersHistory(user));
    }
}
