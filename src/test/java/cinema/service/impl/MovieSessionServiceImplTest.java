package cinema.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import cinema.dao.MovieSessionDao;
import cinema.model.MovieSession;
import cinema.service.MovieSessionService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MovieSessionServiceImplTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 0L;
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2023, 6, 16, 19, 0);
    private static MovieSessionService movieSessionService;
    private static MovieSessionDao movieSessionDao;
    private static MovieSession session;

    @BeforeAll
    static void setUpBeforeClass() {
        movieSessionDao = Mockito.mock(MovieSessionDao.class);
        movieSessionService = new MovieSessionServiceImpl(movieSessionDao);
        session = new MovieSession();
        session.setShowTime(DATE_TIME);
        session.setId(VALID_ID);
    }

    @Test
    void findAvailableSessions_ok() {
        Mockito.when(movieSessionDao.findAvailableSessions(VALID_ID, DATE_TIME.toLocalDate()))
                .thenReturn(List.of(session));
        List<MovieSession> actual = movieSessionService.findAvailableSessions(VALID_ID,
                DATE_TIME.toLocalDate());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(List.of(session), actual);
        
    }

    @Test
    void add_ok() {
        Mockito.when(movieSessionDao.add(session)).thenReturn(session);
        MovieSession actual = movieSessionService.add(session);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(session, actual);
    }

    @Test
    void get_existId_ok() {
        Mockito.when(movieSessionDao.get(VALID_ID)).thenReturn(Optional.of(session));
        MovieSession actual = movieSessionService.get(VALID_ID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(session.getShowTime(), actual.getShowTime());
    }

    @Test
    void get_notExistId_runtimeException() {
        Mockito.when(movieSessionDao.get(VALID_ID)).thenReturn(Optional.of(session));
        Assertions.assertThrows(RuntimeException.class, () -> movieSessionService.get(INVALID_ID));
    }

    @Test
    void update_ok() {
        Mockito.when(movieSessionDao.update(session)).thenReturn(session);
        LocalDateTime showTime = movieSessionService.update(session).getShowTime();
        session.setShowTime(showTime.plusDays(5));
        LocalDateTime updatedShowTime = movieSessionService.update(session).getShowTime();
        Assertions.assertNotEquals(showTime, updatedShowTime);
    }

    @Test
    void delete_ok() {
        Mockito.doNothing().when(movieSessionDao).delete(VALID_ID);
        movieSessionService.delete(VALID_ID);
        verify(movieSessionDao, times(1)).delete(VALID_ID);
    }
}
