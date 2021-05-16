package ua.nazar.rep.libcontrol.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class OrderServiceTest {
    private final OrderService orderService;

    @MockBean
    private OrderRepo orderRepo;

    @Autowired
    public OrderServiceTest(OrderService orderService) {
        this.orderService = orderService;
    }

    private final User testUser = setupUser();
    private final Book testBook = setupBook();
    private final Order testOrder = setupOrder();

    private static User setupUser() {
        User user = new User();
        user.setUsername("NewTestClient");
        user.setPassword("123");
        user.setEmail("some@some.some");
        user.setRoles(Collections.singleton(Role.ROLE_CLIENT));
        return user;
    }

    private static Book setupBook() {
        Book book = new Book();
        book.setInStock(true);
        book.setCode("1111");
        book.setName("New Test");
        return book;
    }

    private Order setupOrder() {
        Order order = new Order();
        order.setClient(testUser);
        order.setBook(testBook);
        return order;
    }

    @Test
    void addOrder() {
        orderService.addOrder(testOrder);

        Assertions.assertNotNull(testOrder);
        Assertions.assertEquals("NewTestClient", testOrder.getClient().getUsername());
        Assertions.assertEquals(Collections.singleton(Role.ROLE_CLIENT), testOrder.getClient().getRoles());
        Assertions.assertEquals("1111", testOrder.getBook().getCode());
        Assertions.assertEquals("New Test", testOrder.getBook().getName());
        Assertions.assertNotNull(testOrder.getDate());
        Assertions.assertEquals("Pending approval", testOrder.getStatus());

        Assertions.assertTrue(testOrder.getBook().isInStock());
        Assertions.assertFalse(testOrder.isForReturn());

        Mockito.verify(orderRepo, Mockito.times(1)).save(testOrder);
    }

    @Test
    void approveOrder() {
        testOrder.setStatus("Pending approval");
        Mockito.when(orderRepo.findById(Mockito.any(Long.class))).thenReturn(java.util.Optional.of(testOrder));

        orderService.approveOrder(1L);

        Assertions.assertTrue(testOrder.isApproved());
        Assertions.assertFalse(testOrder.getBook().isInStock());
        Assertions.assertEquals("Approved", testOrder.getStatus());

        Mockito.verify(orderRepo, Mockito.times(1)).save(testOrder);
    }

    @Test
    void confirmOrder() {
        testOrder.setStatus("Pending approval");
        Mockito.when(orderRepo.findById(Mockito.any(Long.class))).thenReturn(java.util.Optional.of(testOrder));

        orderService.confirmOrder(testUser, 1L);

        Assertions.assertTrue(testOrder.isConfirmed());

        Assertions.assertTrue(testOrder.isFinished());
        Assertions.assertNotNull(testOrder.getBook().getOwner());
        Assertions.assertEquals(testUser, testOrder.getBook().getOwner());
        Assertions.assertEquals("Finished", testOrder.getStatus());

        Mockito.verify(orderRepo, Mockito.times(1)).save(testOrder);
    }

    @Test
    void addRequest() {
        testOrder.setForReturn(true);
        testOrder.getBook().setInStock(false);

        orderService.addRequest(testOrder);

        Assertions.assertNotNull(testOrder);
        Assertions.assertEquals("NewTestClient", testOrder.getClient().getUsername());
        Assertions.assertEquals(Collections.singleton(Role.ROLE_CLIENT), testOrder.getClient().getRoles());
        Assertions.assertEquals("1111", testOrder.getBook().getCode());
        Assertions.assertEquals("New Test", testOrder.getBook().getName());
        Assertions.assertNotNull(testOrder.getDate());
        Assertions.assertEquals("Pending approval", testOrder.getStatus());

        Assertions.assertTrue(testOrder.isForReturn());
        Assertions.assertTrue(testOrder.isConfirmed());
        Assertions.assertNull(testOrder.getBook().getOwner());
        Assertions.assertFalse(testOrder.getBook().isInStock());

        Mockito.verify(orderRepo, Mockito.times(1)).save(testOrder);
    }

    @Test
    void approveRequest() {
        testOrder.setForReturn(true);
        Mockito.when(orderRepo.findById(Mockito.any(Long.class))).thenReturn(java.util.Optional.of(testOrder));

        orderService.approveRequest(1L);

        Assertions.assertTrue(testOrder.isApproved());
        Assertions.assertTrue(testOrder.isFinished());
        Assertions.assertTrue(testOrder.getBook().isInStock());
        Assertions.assertEquals("Finished", testOrder.getStatus());

        Mockito.verify(orderRepo, Mockito.times(1)).save(testOrder);
    }
}