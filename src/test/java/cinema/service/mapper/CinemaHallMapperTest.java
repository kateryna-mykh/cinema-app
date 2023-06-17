package cinema.service.mapper;

import cinema.dto.request.CinemaHallRequestDto;
import cinema.dto.response.CinemaHallResponseDto;
import cinema.model.CinemaHall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class CinemaHallMapperTest {
    private static CinemaHallMapper cinemaHallMapper;
    private static CinemaHallRequestDto dto;
    private static CinemaHall cinemaHall;

    @BeforeAll
    static void setUpBeforeClass() {
        cinemaHallMapper = new CinemaHallMapper();
        cinemaHall = new CinemaHall();
        cinemaHall.setId(1L);
        cinemaHall.setDescription("Multiplex");
        cinemaHall.setCapacity(200);
        dto = new CinemaHallRequestDto();
        ReflectionTestUtils.setField(dto, "capacity", 150);
        ReflectionTestUtils.setField(dto, "description", "Pioneer");
    }

    @Test
    void mapToModel_ok() {
        CinemaHall actual = cinemaHallMapper.mapToModel(dto);
        Assertions.assertEquals(dto.getCapacity(), actual.getCapacity());
        Assertions.assertEquals(dto.getDescription(), actual.getDescription());
    }

    @Test
    void mapToDto_ok() {
        CinemaHallResponseDto actual = cinemaHallMapper.mapToDto(cinemaHall);
        Assertions.assertEquals(cinemaHall.getId(), actual.getId());
        Assertions.assertEquals(cinemaHall.getCapacity(), actual.getCapacity());
        Assertions.assertEquals(cinemaHall.getDescription(), actual.getDescription());
    }
}
