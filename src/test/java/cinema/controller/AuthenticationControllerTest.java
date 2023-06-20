package cinema.controller;

import cinema.dto.request.UserRequestDto;
import cinema.dto.response.UserResponseDto;
import cinema.model.User;
import cinema.service.AuthenticationService;
import cinema.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AuthenticationControllerTest {
    private static final Long ID = 2L;
    private static final String EMAIL = "test-email@i.ua";
    private static final String PASSWORD = "testpassword";
    private static AuthenticationController authenticationController;
    private static AuthenticationService authService;
    private static MockMvc mockMvc;
    private static UserMapper mapper;
    private static UserRequestDto requestDto;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        authService = Mockito.mock(AuthenticationService.class);
        requestDto = new UserRequestDto();
        ReflectionTestUtils.setField(requestDto, "email", EMAIL);
        ReflectionTestUtils.setField(requestDto, "password", PASSWORD);
        ReflectionTestUtils.setField(requestDto, "repeatPassword", PASSWORD);
        mapper = Mockito.mock(UserMapper.class);
        authenticationController = new AuthenticationController(authService, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void register_ok() throws Exception {
        User user = new User();
        user.setId(ID);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(ID);
        responseDto.setEmail(EMAIL);
        Mockito.when(authService.register(requestDto.getEmail(), requestDto.getPassword()))
                .thenReturn(user);
        Mockito.when(mapper.mapToDto(user)).thenReturn(responseDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/register").requestAttr("requestDto", requestDto))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(responseDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(responseDto.getEmail()))
                .andReturn();
        // .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
