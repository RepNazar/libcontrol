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

    public Page<Book> findAllBooks(Pageable pageable){
        return bookRepo.findAll(pageable);
    }

    public Page<Book> findAllBooksByNameContains(String filter, Pageable pageable){
        return bookRepo.findAllByNameContains(filter, pageable);
    }

    public Book findBookByIdAndInStockTrue(Long id){
        return bookRepo.findByIdAndInStockTrue(id);
    }

    public Page<Book> findAllBooksByOwnerIdAndNameContains(Long ownerId, String filter, Pageable pageable){
        return bookRepo.findAllByOwnerIdAndNameContains(ownerId, filter, pageable);
    }

    public void deleteById(Long id){
        bookRepo.deleteById(id);
    }

    public Book saveBook(Book book){
        return bookRepo.save(book);
    }

}
