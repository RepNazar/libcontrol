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
    private final User userMock = Mockito.mock(User.class);

    @Test
    void findAllBooks() {
        bookService.findAllBooks(pageableMock);
        Mockito.verify(bookRepo, Mockito.times(1)).findAll(pageableMock);
    }

    @Test
    void findAllBooksByNameContains() {
        bookService.findAllBooksByNameContains("qwer", pageableMock);
        Mockito.verify(bookRepo, Mockito.times(1)).findAllByNameContains("qwer", pageableMock);
    }

    @Test
    void findBookByIdAndInStockTrue() {
        bookService.findBookByIdAndInStockTrue(1L);
        Mockito.verify(bookRepo, Mockito.times(1)).findByIdAndInStockTrue(1L);
    }

    @Test
    void findAllBooksByOwnerIdAndNameContains() {
        bookService.findAllBooksByOwnerIdAndNameContains(1L, "qwer", pageableMock);
        Mockito.verify(bookRepo, Mockito.times(1)).findAllByOwnerIdAndNameContains(1L,"qwer", pageableMock);
    }

    @Test
    void deleteById() {
        bookService.deleteById(1L);
        Mockito.verify(bookRepo, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void saveBook() {
        Book book = Mockito.mock(Book.class);
        bookService.saveBook(book);
        Mockito.verify(bookRepo, Mockito.times(1)).save(book);
    }
}