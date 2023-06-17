package cinema.service.mapper;

import cinema.dto.request.MovieSessionRequestDto;
import cinema.dto.response.MovieSessionResponseDto;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.service.CinemaHallService;
import cinema.service.MovieService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class MovieSessionMapperTest {
    private static final Long ID = 1L;
    private static MovieSessionMapper movieSessionMapper;
    private static CinemaHallService cinemaHallService;
    private static MovieService movieService;
    private static MovieSessionRequestDto dto;
    private static MovieSession movieSession;
    private static Movie movie;
    private static CinemaHall cinemaHall;

    @BeforeAll
    static void setUpBeforeClass() {
        cinemaHallService = Mockito.mock(CinemaHallService.class);
        movieService = Mockito.mock(MovieService.class);
        movieSessionMapper = new MovieSessionMapper(cinemaHallService, movieService);
        movieSession = new MovieSession();
        movieSession.setId(ID);
        cinemaHall = new CinemaHall();
        cinemaHall.setId(ID);
        movieSession.setCinemaHall(cinemaHall);
        movie = new Movie();
        movie.setId(ID);
        movie.setTitle("Mavka. The Forest Song");
        movieSession.setMovie(movie);
        movieSession.setShowTime(LocalDateTime.of(2023, 6, 20, 19, 0));
        dto = new MovieSessionRequestDto();
        ReflectionTestUtils.setField(dto, "movieId", ID);
        ReflectionTestUtils.setField(dto, "cinemaHallId", ID);
        ReflectionTestUtils.setField(dto, "showTime", LocalDateTime.of(2023, 6, 22, 15, 0));
    }

    @Test
    void mapToModel_ok() {
        Mockito.when(movieService.get(dto.getMovieId())).thenReturn(movie);
        Mockito.when(cinemaHallService.get(dto.getCinemaHallId())).thenReturn(cinemaHall);
        MovieSession actual = movieSessionMapper.mapToModel(dto);
        Assertions.assertEquals(dto.getMovieId(), actual.getMovie().getId());
        Assertions.assertEquals(dto.getCinemaHallId(), actual.getCinemaHall().getId());
        Assertions.assertEquals(dto.getShowTime(), actual.getShowTime());

    }

    @Test
    void mapToDto_ok() {
        MovieSessionResponseDto actual = movieSessionMapper.mapToDto(movieSession);
        Assertions.assertEquals(movieSession.getId(), actual.getMovieSessionId());
        Assertions.assertEquals(movieSession.getCinemaHall().getId(), actual.getCinemaHallId());
        Assertions.assertEquals(movieSession.getMovie().getId(), actual.getMovieId());
        Assertions.assertEquals(movieSession.getMovie().getTitle(), actual.getMovieTitle());
        Assertions.assertEquals(movieSession.getShowTime(), actual.getShowTime());
    }
}
