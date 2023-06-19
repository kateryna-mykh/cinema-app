package cinema.dao.impl;

import cinema.dao.CinemaHallDao;
import cinema.dao.MovieDao;
import cinema.dao.MovieSessionDao;
import cinema.exception.DataProcessingException;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MovieSessionDaoImplTest extends AbstractTest {
    private MovieSessionDao movieSessionDao;
    private MovieDao movieDao;
    private CinemaHallDao cinemaHallDao;
    private MovieSession movieSession;
    private LocalDateTime time = LocalDateTime.of(2023, 6, 16, 19, 0);
    private SessionFactory sessionFactory;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { MovieSession.class, Movie.class, CinemaHall.class };
    }

    @BeforeEach
    void setUp() {
        movieSessionDao = new MovieSessionDaoImpl(getSessionFactory());
        movieDao = new MovieDaoImpl(getSessionFactory());
        cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());

        Movie mavka = new Movie();
        mavka.setTitle(MOVIE_TITLE);
        movieDao.add(mavka);
        CinemaHall multiplex = new CinemaHall();
        multiplex.setDescription(CINEMA_HALL_DESCR);
        multiplex.setCapacity(150);
        cinemaHallDao.add(multiplex);
        movieSession = new MovieSession();
        movieSession.setMovie(mavka);
        movieSession.setCinemaHall(multiplex);
        movieSession.setShowTime(time);
    }

    @Test
    void findAvailableSessions_ok() {
        movieSessionDao.add(movieSession);
        List<MovieSession> actual = movieSessionDao.findAvailableSessions(TEST_ID, time.toLocalDate());
        Assertions.assertEquals(List.of(movieSession).size(), actual.size());
    }
    
    @Test
    void findAvailableSessions_exception_dataProcessingException() {   
        sessionFactory = Mockito.mock(SessionFactory.class);
        movieSessionDao = new MovieSessionDaoImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenThrow(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class, () -> movieSessionDao.findAvailableSessions(TEST_ID, time.toLocalDate()));
    }

    @Test
    void delete_ok() {
        movieSessionDao.add(movieSession);
        movieSessionDao.add(movieSession);
        movieSessionDao.delete(TEST_ID);
        Optional<MovieSession> deletedSession = movieSessionDao.get(TEST_ID);
        Assertions.assertFalse(deletedSession.isPresent());
        Assertions.assertTrue(movieSessionDao.get(2L).isPresent());
    }
    
    @Test
    void delete_transactionFail_dataProcessingException() {   
        Assertions.assertThrows(DataProcessingException.class, () -> movieSessionDao.delete(null));
    }
    
    @Test
    void delete_exception_dataProcessingException() {   
        sessionFactory = Mockito.mock(SessionFactory.class);
        movieSessionDao = new MovieSessionDaoImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenThrow(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class, () -> movieSessionDao.delete(TEST_ID));
    }
}
