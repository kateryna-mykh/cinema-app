package cinema.dao.impl;

import cinema.dao.MovieDao;
import cinema.model.Movie;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieDaoImplTest extends AbstractTest {
    private final String TITLE = " Mavka. The Forest Song";
    private static final Long ID = 1L;
    private MovieDao movieDao;
    private Movie movie;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { Movie.class };
    }

    @BeforeEach
    void setUp() {
        movieDao = new MovieDaoImpl(getSessionFactory());
        movie = new Movie();
        movie.setTitle(TITLE);
    }

    @Test
    void add_ok() {
        Movie actual = movieDao.add(movie);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, movie.getId());
    }

    @Test
    void get_existingMovie_ok() {
        movieDao.add(movie);
        Movie actual = movieDao.get(ID).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(movie.getDescription(), actual.getDescription());
        Assertions.assertEquals(movie.getTitle(), actual.getTitle());
    }

    @Test
    void get_notExistingMovie_isNotPresent() {
        Optional<Movie> actual = movieDao.get(ID);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, () -> movieDao.get(ID).get());
    }

    @Test
    void getAll_ok() {
        movieDao.add(movie);
        List<Movie> actual = movieDao.getAll();
        Assertions.assertEquals(List.of(movie), actual);
    }
}
