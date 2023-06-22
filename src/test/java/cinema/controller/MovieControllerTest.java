package cinema.controller;

import cinema.dto.request.MovieRequestDto;
import cinema.dto.response.MovieResponseDto;
import cinema.model.Movie;
import cinema.service.MovieService;
import cinema.service.mapper.MovieMapper;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MovieControllerTest {
    private static MovieController movieController;
    private static MovieService movieService;
    private static MockMvc mockMvc;
    private static MovieMapper mapper;
    private static MovieRequestDto requestDto;
    private static Movie movie;

    @BeforeAll
    static void setUpBeforeClass() {
        movieService = Mockito.mock(MovieService.class);
        mapper = Mockito.mock(MovieMapper.class);
        movieController = new MovieController(movieService, mapper, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
        requestDto = Mockito.mock(MovieRequestDto.class);
        movie = Mockito.mock(Movie.class);
    }

    @Test
    void add_ok() throws Exception {
        Mockito.when(mapper.mapToModel(requestDto)).thenReturn(movie);
        Mockito.when(movieService.add(movie)).thenReturn(movie);
        mockMvc.perform(MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_JSON).content(
                        "{\"title\":\"The Little Marmaid, 2023\", \"description\":\"The Little Mermaid is a 2023 American musical fantasy film directed by Rob Marshall\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void getAll_ok() throws Exception {
        MovieResponseDto responseDto = Mockito.mock(MovieResponseDto.class);
        Mockito.when(mapper.mapToDto(movie)).thenReturn(responseDto);
        Mockito.when(movieService.getAll()).thenReturn(List.of(movie));
        mockMvc.perform(MockMvcRequestBuilders.get("/movies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andReturn();
    }
}
