package cinema.dao.impl;

import cinema.dao.CinemaHallDao;
import cinema.model.CinemaHall;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CinemaHallDaoImplTest extends AbstractTest {
    private static final String DESCRIPTION = "Multiplex";
    private static final int CAPACITY = 150;
    private static final Long ID = 1L;
    private CinemaHallDao cinemaHallDao;
    private CinemaHall cinemaHall;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { CinemaHall.class };
    }

    @BeforeEach
    void setUp() {
        cinemaHallDao = new CinemaHallDaoImpl(getSessionFactory());
        cinemaHall = new CinemaHall();
        cinemaHall.setDescription(DESCRIPTION);
        cinemaHall.setCapacity(CAPACITY);
    }

    @Test
    void add_ok() {
        CinemaHall actual = cinemaHallDao.add(cinemaHall);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, cinemaHall.getId());
    }

    @Test
    void get_existingCinemaHall_ok() {
        cinemaHallDao.add(cinemaHall);
        CinemaHall actual = cinemaHallDao.get(ID).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(cinemaHall.getDescription(), actual.getDescription());
        Assertions.assertEquals(cinemaHall.getCapacity(), actual.getCapacity());
    }

    @Test
    void get_notExistingCinemaHall_isNotPresent() {
        Optional<CinemaHall> actual = cinemaHallDao.get(ID);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, () -> cinemaHallDao.get(ID).get());
    }

    @Test
    void getAll_ok() {
        cinemaHallDao.add(cinemaHall);
        List<CinemaHall> actual = cinemaHallDao.getAll();
        Assertions.assertEquals(List.of(cinemaHall), actual);
    }
}
