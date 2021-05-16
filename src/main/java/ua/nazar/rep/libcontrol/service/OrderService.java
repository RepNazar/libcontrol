package ua.nazar.rep.libcontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.nazar.rep.libcontrol.domain.Book;
import ua.nazar.rep.libcontrol.domain.Order;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.repo.OrderRepo;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepo orderRepo;

    @Autowired
    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Page<Order> findAllOrders(Pageable pageable) {
        return orderRepo.findAllByForReturn(false, pageable);
    }

    public Page<Order> findAllRequests(Pageable pageable) {
        return orderRepo.findAllByForReturn(true, pageable);
    }

    public Page<Order> findAllByClientId(Long user_id, Pageable pageable) {
        return orderRepo.findAllByClientIdAndForReturn(user_id, false, pageable);
    }

    public Order addOrder(Order order) {
        order.setStatus("Pending approval");
        order.setDate(LocalDateTime.now());
        return orderRepo.save(order);
    }

    public Order approveOrder(Long order_id) {
        Order order = orderRepo.findById(order_id).get();
        order.setApproved(true);
        order.getBook().setInStock(false);
        order.setStatus("Approved");
        return orderRepo.save(order);
    }

    public Order confirmOrder(User currentUser, Long order_id) {
        Order order = orderRepo.findById(order_id).get();
        order.setConfirmed(true);
        order.getBook().setOwner(currentUser);
        order.setFinished(true);
        order.setStatus("Finished");
        return orderRepo.save(order);
    }

    //FIXME Remove book from owner list
    public Order addRequest(Order request){
        request.setForReturn(true);
        request.setStatus("Pending approval");
        request.setConfirmed(true);
        request.getBook().setOwner(null);
        request.setDate(LocalDateTime.now());
        return orderRepo.save(request);
    }

    public Order approveRequest(Long order_id){
        Order order = orderRepo.findById(order_id).get();
        order.setApproved(true);
        order.getBook().setInStock(true);
        order.setFinished(true);
        order.setStatus("Finished");
        return orderRepo.save(order);
    }
}