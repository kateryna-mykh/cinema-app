package cinema.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import cinema.dao.MovieDao;
import cinema.exception.DataProcessingException;
import cinema.model.Movie;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

class MovieDaoImplTest extends AbstractTest {
    private MovieDao movieDao;
    private Movie movie;
    private SessionFactory sessionFactory;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { Movie.class };
    }

    @BeforeEach
    void setUp() {
        movieDao = new MovieDaoImpl(getSessionFactory());
        movie = new Movie();
        movie.setTitle(MOVIE_TITLE);
    }

    @Test
    void add_ok() {
        Movie actual = movieDao.add(movie);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_ID, movie.getId());
    }

    @Test
    void add_exception_dataProcessingException() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        movieDao = new MovieDaoImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenThrow(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class, () -> movieDao.add(null));
    }

    @Test
    void add_transactionFail_dataProcessingException() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        Session session = Mockito.mock(Session.class);
        Transaction transaction = Mockito.mock(Transaction.class);
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
        Mockito.when(session.beginTransaction()).thenReturn(transaction);
        Mockito.when(session.save(null)).thenReturn(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class, () -> movieDao.add(null));
    }

    @Test
    void get_existingMovie_ok() {
        movieDao.add(movie);
        Movie actual = movieDao.get(TEST_ID).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(movie.getDescription(), actual.getDescription());
        Assertions.assertEquals(movie.getTitle(), actual.getTitle());
    }

    @Test
    void get_notExistingMovie_isNotPresent() {
        Optional<Movie> actual = movieDao.get(TEST_ID);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, () -> movieDao.get(TEST_ID).get());
    }

    @Test
    void get_exception_dataProcessingException() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        movieDao = new MovieDaoImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenThrow(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class, () -> movieDao.get(0L));
    }

    @Test
    void getAll_ok() {
        movieDao.add(movie);
        List<Movie> actual = movieDao.getAll();
        Assertions.assertEquals(List.of(movie), actual);
    }

    @Test
    void getAll_exception_dataProcessingException() {
        sessionFactory = Mockito.mock(SessionFactory.class);
        movieDao = new MovieDaoImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenThrow(HibernateException.class);
        Assertions.assertThrows(DataProcessingException.class, () -> movieDao.getAll());
    }
}
