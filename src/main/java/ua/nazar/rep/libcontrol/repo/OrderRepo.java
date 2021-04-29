package ua.nazar.rep.libcontrol.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nazar.rep.libcontrol.domain.Order;
import ua.nazar.rep.libcontrol.domain.User;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findAllByClient(User client);
    List<Order> findAllByClientId(Long client_id);
}
