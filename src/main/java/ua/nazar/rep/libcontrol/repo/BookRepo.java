package ua.nazar.rep.libcontrol.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nazar.rep.libcontrol.domain.Book;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {
    Book findByIdAndInStockTrue(Long id);
    List<Book> findAllByOwnerId(Long ownerId);
}
