package ua.nazar.rep.libcontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.nazar.rep.libcontrol.domain.Book;
import ua.nazar.rep.libcontrol.repo.BookRepo;

@Service
public class BookService {
    private final BookRepo bookRepo;

    @Autowired
    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Page<Book> findAll(Pageable pageable){
        return bookRepo.findAll(pageable);
    }

    public Page<Book> findAllByFilters(String filter, String genreFilter, Pageable pageable){
        return bookRepo.findAllByNameContainsIgnoreCaseAndGenreContainsIgnoreCase(filter, genreFilter, pageable);
    }

    public Book findByIdAndInStockTrue(Long id){
        return bookRepo.findByIdAndInStockTrue(id);
    }

    public Page<Book> findAllByOwnerIdAndFilters(Long ownerId, String filter, String genreFilter, Pageable pageable){
        return bookRepo.findAllByOwnerIdAndNameContainsIgnoreCaseAndGenreContainsIgnoreCase(ownerId, filter, genreFilter, pageable);
    }

    public void deleteById(Long id){
        bookRepo.deleteById(id);
    }

    public Book save(Book book){
        return bookRepo.save(book);
    }

}
