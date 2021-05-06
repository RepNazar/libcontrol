package ua.nazar.rep.libcontrol.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.nazar.rep.libcontrol.domain.Book;
import ua.nazar.rep.libcontrol.domain.Order;
import ua.nazar.rep.libcontrol.domain.Role;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.repo.OrderRepo;

import java.util.Collections;

@SpringBootTest
public class OrderServiceTest {
    private final OrderService orderService;


    @MockBean
    OrderRepo orderRepo;

    @Autowired
    public OrderServiceTest(OrderService orderService) {
        this.orderService = orderService;
    }

    @Test
    void addOrder() {
        User user = new User();

        user.setUsername("NewTestClient");
        user.setPassword("123");
        user.setEmail("some@some.some");
        user.setRoles(Collections.singleton(Role.ROLE_CLIENT));

        Book book = new Book();
        book.setInStock(true);
        book.setCode("1111");
        book.setName("New Test");

        Order order = new Order();
        order = orderService.addOrder(user, book);
        Mockito.when(orderRepo.save(ArgumentMatchers.any(Order.class))).thenReturn(order);

/*
        Assertions.assertNotNull(order);
        Assertions.assertEquals(order.getClient().getUsername(), "NewTestClient");
        Assertions.assertEquals(order.getClient().getRoles(), Collections.singleton(Role.ROLE_CLIENT));
        Assertions.assertEquals(order.getBook().getCode(), "1111");
        Assertions.assertEquals(order.getBook().getName(), "New Test");
        Assertions.assertTrue(order.getBook().isInStock());
        Assertions.assertNotNull(order.getDate());
        Assertions.assertEquals(order.getStatus(), "Pending approval");
*/

        Mockito.verify(orderRepo, Mockito.times(1)).save(order);

    }

    @Test
    void approveOrder() {
    }

    @Test
    void confirmOrder() {
    }

    @Test
    void addRequest() {
    }

    @Test
    void approveRequest() {
    }
}