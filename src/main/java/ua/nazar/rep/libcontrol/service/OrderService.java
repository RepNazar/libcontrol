package ua.nazar.rep.libcontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nazar.rep.libcontrol.domain.Book;
import ua.nazar.rep.libcontrol.domain.Order;
import ua.nazar.rep.libcontrol.domain.User;
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

    public List<Order> findAllOrders() {
        return orderRepo.findAllByForReturn(false);
    }

    public List<Order> findAllRequests() {
        return orderRepo.findAllByForReturn(true);
    }

    public List<Order> findAllByClientId(Long user_id) {
        return orderRepo.findAllByClientIdAndForReturn(user_id, false);
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

    public void confirmOrder(User currentUser, Long order_id) {
        Order order = orderRepo.findById(order_id).get();
        order.setConfirmed(true);
        order.getBook().setOwner(currentUser);
        order.setFinished(true);
        order.setStatus("Finished");
        orderRepo.save(order);
    }

    public void addRequest(User currentUser, Book book){
        Order request = new Order();
        request.setForReturn(true);
        request.setStatus("Pending approval");
        request.setBook(book);
        request.setConfirmed(true);
        request.getBook().setOwner(null);
        request.setDate(LocalDateTime.now());
        orderRepo.save(request);
    }

    public void approveRequest(Long order_id){
        Order order = orderRepo.findById(order_id).get();
        order.setApproved(true);
        order.getBook().setInStock(true);
        order.setFinished(true);
        order.setStatus("Finished");
        orderRepo.save(order);
    }
}