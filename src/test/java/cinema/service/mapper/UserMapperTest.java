package cinema.service.mapper;

import cinema.dto.response.UserResponseDto;
import cinema.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UserMapperTest {
    private static UserMapper userMapper;
    private static User user;

    @BeforeAll
    static void setUpBeforeClass() {
        userMapper = new UserMapper();
        user = new User();
        user.setId(1L);
        user.setEmail("test-mail@i.ua");
    }

    @Test
    void mapToDto_ok() {
        UserResponseDto actual = userMapper.mapToDto(user);
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }
}
