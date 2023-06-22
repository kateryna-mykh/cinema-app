package cinema.controller;

import cinema.dto.response.UserResponseDto;
import cinema.model.User;
import cinema.service.UserService;
import cinema.service.mapper.UserMapper;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

class UserControllerTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "test-email@i.ua";
    private static UserController userController;
    private static UserService userService;
    private static User user;
    private static MockMvc mockMvc;
    private static UserMapper mapper;

    @BeforeAll
    static void setUpBeforeClass() {
        userService = Mockito.mock(UserService.class);
        user = new User();
        user.setId(ID);
        user.setEmail(EMAIL);
        mapper = Mockito.mock(UserMapper.class);
        userController = new UserController(userService, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void findByEmail_ok() throws Exception {
        UserResponseDto responseDto = Mockito.mock(UserResponseDto.class);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(mapper.mapToDto(user)).thenReturn(responseDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/by-email").param("email", EMAIL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void findByEmail_notExistedEmail_runtimeException() throws Exception {
        String msgAnswer = "User with email " + EMAIL + " not found";
        Mockito.when(userService.findByEmail(EMAIL))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, msgAnswer));
        mockMvc.perform(MockMvcRequestBuilders.get("/users/by-email").param("email", EMAIL))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(exc -> Assertions.assertThrows(RuntimeException.class,
                        () -> userService.findByEmail(EMAIL)))
                .andExpect(msg -> Assertions.assertEquals(true,
                        msg.getResolvedException().getMessage().contains(msgAnswer)))
                .andReturn();
    }
}
