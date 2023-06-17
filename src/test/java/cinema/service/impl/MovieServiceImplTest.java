package cinema.service.impl;

import cinema.dao.MovieDao;
import cinema.model.Movie;
import cinema.service.MovieService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MovieServiceImplTest {
    private static final String TITLE = "Mavka. The Forest Song";
    private static final Long ID = 1L;
    private static final Long INVALID_ID = 0L;
    private static MovieService movieService;
    private static MovieDao movieDao;
    private static Movie movie;

    @BeforeAll
    static void setUpBeforeClass() {
        movieDao = Mockito.mock(MovieDao.class);
        movieService = new MovieServiceImpl(movieDao);
        movie = new Movie();
        movie.setTitle(TITLE);
        movie.setId(ID);
    }

    @Test
    void add_ok() {
        Mockito.when(movieDao.add(movie)).thenReturn(movie);
        Movie actual = movieService.add(movie);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(movie, actual);
    }

    @Test
    void get_existId_ok() {
        Mockito.when(movieDao.get(ID)).thenReturn(Optional.of(movie));
        Movie actual = movieService.get(ID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(movie.getTitle(), actual.getTitle());
    }

    @Test
    void get_notExistId_runtimeException() {
        Mockito.when(movieDao.get(ID)).thenReturn(Optional.of(movie));
        Assertions.assertThrows(RuntimeException.class, () -> movieService.get(INVALID_ID));
    }

    @Test
    void getAll_ok() {
        Mockito.when(movieDao.getAll()).thenReturn(List.of(movie));
        List<Movie> actual = movieService.getAll();
        Assertions.assertEquals(List.of(movie), actual);
    }
}
