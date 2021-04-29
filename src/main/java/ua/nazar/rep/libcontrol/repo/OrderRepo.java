package ua.nazar.rep.libcontrol.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nazar.rep.libcontrol.domain.Order;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findAllByForReturn(boolean forReturn);
    List<Order> findAllByClientIdAndForReturn(Long client_id, boolean forReturn);
}
