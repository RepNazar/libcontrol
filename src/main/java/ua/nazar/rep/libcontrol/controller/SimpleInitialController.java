package ua.nazar.rep.libcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nazar.rep.libcontrol.domain.Book;
import ua.nazar.rep.libcontrol.domain.Order;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.repo.BookRepo;
import ua.nazar.rep.libcontrol.repo.UserRepo;
import ua.nazar.rep.libcontrol.service.OrderService;

import java.util.List;

@Controller
public class SimpleInitialController {

    private final OrderService orderService;

    private BookRepo bookRepo;

    @Autowired
    public SimpleInitialController(OrderService orderService, UserRepo userRepo, BookRepo bookRepo) {
        this.orderService = orderService;
        this.bookRepo = bookRepo;
    }

    @GetMapping("/my-orders")
    public String userOrders(@AuthenticationPrincipal User user, Model model){
        List<Order> orders = orderService.findAllByClient(user);

        model.addAttribute("orders", orders);
        return "orders";

    }

    @GetMapping("/catalog")
    public String catalog(Model model){
        List<Book> catalog = bookRepo.findAll();

        model.addAttribute("catalog", catalog);

        return "catalog";
    }

    @GetMapping("/catalog/{book}")
    public String getBook(
            Model model,
            @RequestParam(required = false) Book book
    ){
        List<Book> catalog = bookRepo.findAll();

        model.addAttribute("catalog", catalog);
        model.addAttribute("book", book);

        return "catalog";
    }

    @PostMapping("/catalog")
    public String commitRecord(
            Book book,
            Model model
    ) {
            model.addAttribute("book", null);
            bookRepo.save(book);

            List<Book> catalog = bookRepo.findAll();

            model.addAttribute("catalog", catalog);

        return "redirect:/catalog";

    }

    @PostMapping("/catalog/delete")
    public String deleteRecord(
            @AuthenticationPrincipal User currentUser,
            @RequestParam Long id
    ) {
        bookRepo.deleteById(id);
        return "redirect:/catalog";
    }

    @GetMapping("/orders")
    public String ordersList(Model model){
        List<Order> orders = orderService.findAll();

        model.addAttribute("orders", orders);
        return "orders";
    }

    @PostMapping("/order/")
    public String commitOrder(@AuthenticationPrincipal User currentUser,
                              @RequestParam Long book_id,
                              Model model){
        orderService.addOrder(currentUser, book_id);
        List<Order> orders = orderService.findAllByClient(currentUser);
        model.addAttribute("orders", orders);
        return "orders";
    }

}
