package ua.nazar.rep.libcontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nazar.rep.libcontrol.domain.Book;
import ua.nazar.rep.libcontrol.domain.Order;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.repo.BookRepo;
import ua.nazar.rep.libcontrol.repo.OrderRepo;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final BookRepo bookRepo;

    @Autowired
    public OrderService(OrderRepo orderRepo, BookRepo bookRepo) {
        this.orderRepo = orderRepo;
        this.bookRepo = bookRepo;
    }

    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    public List<Order> findAllByClient(User user) {
        return orderRepo.findAllByClient(user);
    }

    public void addOrder(User currentUser, Long book_id) {
        Order order = new Order();
        order.setStatus("Pending approval");
        bookRepo.findById(book_id).ifPresent(order::setBook);
        order.setClient(currentUser);
        order.setDate(LocalDateTime.now());
    }
}