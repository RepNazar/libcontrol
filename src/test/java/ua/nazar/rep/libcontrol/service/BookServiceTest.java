package ua.nazar.rep.libcontrol.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ua.nazar.rep.libcontrol.domain.Book;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.repo.BookRepo;

@SpringBootTest
class BookServiceTest {
    private final BookService bookService;

    @Autowired
    BookServiceTest(BookService bookService) {
        this.bookService = bookService;
    }

    @MockBean
    private BookRepo bookRepo;

    private final Pageable pageableMock = Mockito.mock(Pageable.class);

    @Test
    void findAllTest() {
        bookService.findAll(pageableMock);
        Mockito.verify(bookRepo, Mockito.times(1)).findAll(pageableMock);
    }

    @Test
    void findAllByNameContainsTest() {
        bookService.findAllByFilters("qwer", "req", pageableMock);
        Mockito.verify(bookRepo, Mockito.times(1)).findAllByNameContainsIgnoreCaseAndGenreContainsIgnoreCase("qwer", "req", pageableMock);
    }

    @Test
    void findByIdAndInStockTrueTest() {
        bookService.findByIdAndInStockTrue(1L);
        Mockito.verify(bookRepo, Mockito.times(1)).findByIdAndInStockTrue(1L);
    }

    @Test
    void findAllByOwnerIdAndNameContainsTest() {
        bookService.findAllByOwnerIdAndFilters(1L, "qwer", "req", pageableMock);
        Mockito.verify(bookRepo, Mockito.times(1)).findAllByOwnerIdAndNameContainsIgnoreCaseAndGenreContainsIgnoreCase(1L, "qwer", "req", pageableMock);
    }

    @Test
    void deleteByIdTest() {
        bookService.deleteById(1L);
        Mockito.verify(bookRepo, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void saveTest() {
        Book book = Mockito.mock(Book.class);
        bookService.save(book);
        Mockito.verify(bookRepo, Mockito.times(1)).save(book);
    }
}