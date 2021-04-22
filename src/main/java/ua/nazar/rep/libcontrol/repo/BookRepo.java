package ua.nazar.rep.libcontrol.repo;

import org.springframework.data.repository.CrudRepository;
import ua.nazar.rep.libcontrol.domain.Book;

public interface BookRepo extends CrudRepository<Book, Long> {
}
