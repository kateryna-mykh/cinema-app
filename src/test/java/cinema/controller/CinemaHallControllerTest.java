package cinema.controller;

import cinema.dto.request.CinemaHallRequestDto;
import cinema.dto.response.CinemaHallResponseDto;
import cinema.model.CinemaHall;
import cinema.service.CinemaHallService;
import cinema.service.mapper.CinemaHallMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class CinemaHallControllerTest {
    private static CinemaHallController cinemaHallController;
    private static CinemaHallService cinemaHallService;
    private static MockMvc mockMvc;
    private static CinemaHallMapper mapper;
    private static CinemaHallRequestDto requestDto;
    private static CinemaHall cinemaHall;

    @BeforeAll
    static void setUpBeforeClass() {
        cinemaHallService = Mockito.mock(CinemaHallService.class);
        mapper = Mockito.mock(CinemaHallMapper.class);
        cinemaHallController = new CinemaHallController(cinemaHallService, mapper, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(cinemaHallController).build();
        requestDto = Mockito.mock(CinemaHallRequestDto.class);
        cinemaHall = Mockito.mock(CinemaHall.class);
    }

    @Test
    void add_ok() throws Exception {   
        Mockito.when(mapper.mapToModel(requestDto)).thenReturn(cinemaHall);
        Mockito.when(cinemaHallService.add(cinemaHall)).thenReturn(cinemaHall);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/cinema-halls").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"capacity\":200, \"description\":\"Multiplex\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void getAll_ok() throws Exception {
        CinemaHallResponseDto responseDto = Mockito.mock(CinemaHallResponseDto.class);        
        Mockito.when(mapper.mapToDto(cinemaHall)).thenReturn(responseDto);
        Mockito.when(cinemaHallService.getAll()).thenReturn(List.of(cinemaHall));
        mockMvc.perform(MockMvcRequestBuilders.get("/cinema-halls"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
