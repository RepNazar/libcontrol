package ua.nazar.rep.libcontrol.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nazar.rep.libcontrol.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
