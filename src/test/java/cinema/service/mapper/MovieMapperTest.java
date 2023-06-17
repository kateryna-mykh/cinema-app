package cinema.service.mapper;

import cinema.dto.request.MovieRequestDto;
import cinema.dto.response.MovieResponseDto;
import cinema.model.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class MovieMapperTest {
    private static MovieMapper movieMapper;
    private static MovieRequestDto dto;
    private static Movie movie;

    @BeforeAll
    static void setUpBeforeClass() {
        movieMapper = new MovieMapper();
        movie = new Movie();
        movie.setTitle("Mavka. The Forest Song");
        movie.setId(1L);
        dto = new MovieRequestDto();
        ReflectionTestUtils.setField(dto, "title", "The Little Marmaid");
        ReflectionTestUtils.setField(dto, "description", "2023");
    }

    @Test
    void mapToModel_ok() {
        Movie actual = movieMapper.mapToModel(dto);
        Assertions.assertEquals(dto.getTitle(), actual.getTitle());
        Assertions.assertEquals(dto.getDescription(), actual.getDescription());
    }

    @Test
    void mapToDto_ok() {
        MovieResponseDto actual = movieMapper.mapToDto(movie);
        Assertions.assertEquals(movie.getId(), actual.getId());
        Assertions.assertEquals(movie.getTitle(), actual.getTitle());
        Assertions.assertEquals(movie.getDescription(), actual.getDescription());
    }
}
