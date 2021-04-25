package ua.nazar.rep.libcontrol.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nazar.rep.libcontrol.domain.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
