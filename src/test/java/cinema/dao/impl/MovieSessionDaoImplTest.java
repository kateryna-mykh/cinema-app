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
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieSessionDaoImplTest extends AbstractTest {
    private static final Long ID = 1L;
    private MovieSessionDao movieSessionDao;
    private MovieDao movieDao;
    private CinemaHallDao cinemaHallDao;
    private MovieSession movieSession;
    private LocalDateTime time = LocalDateTime.of(2023, 6, 16, 19, 0);

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
        mavka.setTitle("Mavka. The Forest Song");
        movieDao.add(mavka);
        CinemaHall multiplex = new CinemaHall();
        multiplex.setDescription("Multiplex");
        multiplex.setCapacity(150);
        cinemaHallDao.add(multiplex);
        movieSession = new MovieSession();
        movieSession.setMovie(mavka);
        movieSession.setCinemaHall(multiplex);
        movieSession.setShowTime(time);
    }

    @Test
    void add_ok() {
        MovieSession actual = movieSessionDao.add(movieSession);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void get_existingMovieSession_ok() {
        movieSessionDao.add(movieSession);
        MovieSession actual = movieSessionDao.get(ID).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(movieSession.getShowTime().getDayOfMonth(),
                actual.getShowTime().getDayOfMonth());
        Assertions.assertEquals(movieSession.getShowTime().getHour(),
                actual.getShowTime().getHour());
        Assertions.assertEquals(movieSession.getShowTime().getMinute(),
                actual.getShowTime().getMinute());
    }

    @Test
    void get_notExistingMovieSession_isNotPresent() {
        Optional<MovieSession> actual = movieSessionDao.get(ID);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, () -> actual.get());
    }

    @Test
    void findAvailableSessions_ok() {
        movieSessionDao.add(movieSession);
        List<MovieSession> actual = movieSessionDao.findAvailableSessions(ID, time.toLocalDate());
        Assertions.assertEquals(List.of(movieSession).size(), actual.size());
    }

    @Test
    void update_updatingShowTime_ok() {
        String session = movieSessionDao.add(movieSession).toString();
        LocalDateTime changedTime = time.plusDays(4);
        movieSession.setShowTime(changedTime);
        movieSessionDao.update(movieSession);
        String updatedSession = movieSessionDao.get(ID).get().toString();
        Assertions.assertNotEquals(session, updatedSession);
    }

    @Test
    void update_nullValue_dataProcessingException() {
        Assertions.assertThrows(DataProcessingException.class, () -> movieSessionDao.update(null));
    }

    @Test
    void delete_ok() {
        movieSessionDao.add(movieSession);
        movieSessionDao.add(movieSession);
        movieSessionDao.delete(ID);
        Optional<MovieSession> deletedSession = movieSessionDao.get(ID);
        Assertions.assertFalse(deletedSession.isPresent());
        Assertions.assertTrue(movieSessionDao.get(2L).isPresent());
    }
    
    @Test
    void delete_nullValue_dataProcessingException() {   
        Assertions.assertThrows(DataProcessingException.class, () -> movieSessionDao.delete(null));
    }
}
