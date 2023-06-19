package cinema.service.mapper;

import cinema.dto.response.ShoppingCartResponseDto;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ShoppingCartMapperTest {
    private static ShoppingCartMapper shoppingCartMapper;
    private static ShoppingCart shoppingCart;

    @BeforeAll
    static void setUpBeforeClass() {
        shoppingCartMapper = new ShoppingCartMapper();
        shoppingCart = new ShoppingCart();
        User user = new User();
        user.setId(1L);
        shoppingCart.setUser(user);
        Ticket ticket = new Ticket();
        List<Ticket> tickets = new ArrayList<>();
        for (Long i = 1L; i < 6; i++) {
            ticket.setId(i);
            tickets.add(ticket);
        }
        shoppingCart.setTickets(tickets);
    }

    @Test
    void mapToDto_ok() {
        ShoppingCartResponseDto actual = shoppingCartMapper.mapToDto(shoppingCart);
        Assertions.assertEquals(shoppingCart.getUser().getId(), actual.getUserId());
        Assertions.assertEquals(shoppingCart.getTickets().size(), actual.getTicketIds().size());
    }
}
