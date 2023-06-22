package cinema.controller;

import cinema.dto.request.MovieSessionRequestDto;
import cinema.dto.response.MovieSessionResponseDto;
import cinema.model.MovieSession;
import cinema.service.MovieSessionService;
import cinema.service.mapper.MovieSessionMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MovieSessionControllerTest {
    private static final Long ID = 2L;
    private static MovieSessionController movieSessionController;
    private static MovieSessionService movieSessionService;
    private static MockMvc mockMvc;
    private static MovieSessionMapper mapper;
    private static MovieSessionRequestDto requestDto;
    private static MovieSessionResponseDto responseDto;
    private static MovieSession movieSession;

    @BeforeAll
    static void setUpBeforeClass() {
        movieSessionService = Mockito.mock(MovieSessionService.class);
        mapper = Mockito.mock(MovieSessionMapper.class);
        movieSessionController = new MovieSessionController(movieSessionService, mapper, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(movieSessionController).build();
        requestDto = Mockito.mock(MovieSessionRequestDto.class);
        responseDto = Mockito.mock(MovieSessionResponseDto.class);
        movieSession = Mockito.mock(MovieSession.class);
        Mockito.when(mapper.mapToModel(Mockito.any(MovieSessionRequestDto.class)))
                .thenReturn(movieSession);
        Mockito.when(mapper.mapToDto(movieSession)).thenReturn(responseDto);
    }

    @Test
    void add_ok() throws Exception {
        Mockito.when(movieSessionService.add(movieSession)).thenReturn(movieSession);
        mockMvc.perform(MockMvcRequestBuilders.post("/movie-sessions")
                .contentType(MediaType.APPLICATION_JSON).content(
                        "{\"movieId\":2, \"cinemaHallId\":1, \"showTime\":\"2023-06-01T12:30:00\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void findAvailableSessions_ok() throws Exception {
        LocalDate date = LocalDate.of(2023, 06, 01);
        Mockito.when(movieSessionService.findAvailableSessions(2L, date))
                .thenReturn(List.of(movieSession));
        mockMvc.perform(MockMvcRequestBuilders.get("/movie-sessions/available")
                .param("movieId", String.valueOf(ID)).param("date", "01.06.2023"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void update_ok() throws Exception {
        Mockito.when(movieSessionService.update(Mockito.any(MovieSession.class)))
                .thenReturn(movieSession);
        mockMvc.perform(MockMvcRequestBuilders.put("/movie-sessions/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON).content(
                        "{\"movieId\":2, \"cinemaHallId\":1, \"showTime\":\"2023-06-01T19:00:00\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void delete_ok() throws Exception {
        Mockito.doNothing().when(movieSessionService).delete(ID);
        mockMvc.perform(MockMvcRequestBuilders.delete("/movie-sessions/{id}", ID))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
}
