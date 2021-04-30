package ua.nazar.rep.libcontrol.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.nazar.rep.libcontrol.domain.Book;

public interface BookRepo extends JpaRepository<Book, Long> {
    Book findByIdAndInStockTrue(Long id);
    Page<Book> findAllByOwnerId(Long ownerId, Pageable pageable);
}
