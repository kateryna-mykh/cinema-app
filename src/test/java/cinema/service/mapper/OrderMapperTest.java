package cinema.service.mapper;

import cinema.dto.response.OrderResponseDto;
import cinema.model.Order;
import cinema.model.Ticket;
import cinema.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OrderMapperTest {
    private static OrderMapper orderMapper;
    private static Order order;

    @BeforeAll
    static void setUpBeforeClass() {
        orderMapper = new OrderMapper();
        order = new Order();
        order.setId(1L);
        User user = new User();
        user.setId(1L);
        order.setUser(user);
        order.setOrderTime(LocalDateTime.of(2023, 6, 16, 19, 0));
        Ticket ticket = new Ticket();
        List<Ticket> tickets = new ArrayList<>();
        for (Long i = 1L; i < 6; i++) {
            ticket.setId(i);
            tickets.add(ticket);
        }
        order.setTickets(tickets);
    }

    @Test
    void mapToDto_ok() {
        OrderResponseDto actual = orderMapper.mapToDto(order);
        Assertions.assertEquals(order.getId(), actual.getId());
        Assertions.assertEquals(order.getTickets().size(), actual.getTicketIds().size());
        Assertions.assertEquals(order.getUser().getId(), actual.getUserId());
        Assertions.assertEquals(order.getOrderTime(), actual.getOrderTime());
    }
}
