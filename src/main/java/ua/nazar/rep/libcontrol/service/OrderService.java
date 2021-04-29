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

    @Autowired
    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    public List<Order> findAllByClient(User user) {
        return orderRepo.findAllByClient(user);
    }

    public List<Order> findAllByClientId(Long user_id) {
        return orderRepo.findAllByClientId(user_id);
    }

    public void addOrder(User currentUser, Book book) {
        Order order = new Order();
        order.setStatus("Pending approval");

        order.setBook(book);
        order.setClient(currentUser);
        order.setDate(LocalDateTime.now());
        orderRepo.save(order);
    }

    public void approveOrder(Long order_id) {
        Order order = orderRepo.findById(order_id).get();
        order.setApproved(true);
        order.getBook().setInStock(false);
        order.setStatus("Approved");
        orderRepo.save(order);
    }

    public void confirmOrder(Long order_id) {
        Order order = orderRepo.findById(order_id).get();
        order.setConfirmed(true);
        order.setFinished(true);
        order.setStatus("Finished");
        orderRepo.save(order);
    }
}