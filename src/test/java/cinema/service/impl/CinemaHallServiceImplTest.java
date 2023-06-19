package cinema.service.impl;

import cinema.dao.CinemaHallDao;
import cinema.model.CinemaHall;
import cinema.service.CinemaHallService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CinemaHallServiceImplTest {
    private static final String DESCRIPTION = "Multiplex";
    private static final int CAPACITY = 200;
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 0L;
    private static CinemaHallService cinemaHallService;
    private static CinemaHallDao cinemaHallDao;
    private static CinemaHall cinemaHall;

    @BeforeAll
    static void setUpBeforeClass() {
        cinemaHallDao = Mockito.mock(CinemaHallDao.class);
        cinemaHallService = new CinemaHallServiceImpl(cinemaHallDao);
        cinemaHall = new CinemaHall();
        cinemaHall.setDescription(DESCRIPTION);
        cinemaHall.setCapacity(CAPACITY);
    }

    @Test
    void add_ok() {
        Mockito.when(cinemaHallDao.add(cinemaHall)).thenReturn(cinemaHall);
        CinemaHall actual = cinemaHallService.add(cinemaHall);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(cinemaHall, actual);
    }

    @Test
    void get_existId_ok() {
        Mockito.when(cinemaHallDao.get(VALID_ID)).thenReturn(Optional.of(cinemaHall));
        CinemaHall actual = cinemaHallService.get(VALID_ID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(cinemaHall.getDescription(), actual.getDescription());
    }

    @Test
    void get_notExistId_runtimeException() {
        Mockito.when(cinemaHallDao.get(VALID_ID)).thenReturn(Optional.of(cinemaHall));
        Assertions.assertThrows(RuntimeException.class, () -> cinemaHallService.get(INVALID_ID));
    }

    @Test
    void getAll_ok() {
        Mockito.when(cinemaHallDao.getAll()).thenReturn(List.of(cinemaHall));
        List<CinemaHall> actual = cinemaHallService.getAll();
        Assertions.assertEquals(List.of(cinemaHall), actual);
    }
}
