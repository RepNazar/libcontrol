package ua.nazar.rep.libcontrol.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.nazar.rep.libcontrol.domain.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
    Page<Order> findAll(Pageable pageable);
    Page<Order> findAllByForReturn(boolean forReturn, Pageable pageable);
    Page<Order> findAllByClientIdAndForReturn(Long client_id, boolean forReturn, Pageable pageable);
}
