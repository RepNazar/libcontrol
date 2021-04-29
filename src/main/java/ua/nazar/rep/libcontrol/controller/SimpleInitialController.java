package ua.nazar.rep.libcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final BookRepo bookRepo;

    @Autowired
    public SimpleInitialController(OrderService orderService, BookRepo bookRepo) {
        this.orderService = orderService;
        this.bookRepo = bookRepo;
    }

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/catalog")
    public String getBook(
            Model model,
            @RequestParam(required = false) Book book
    ) {
        List<Book> catalog = bookRepo.findAll();

        model.addAttribute("catalog", catalog);
        model.addAttribute("book", book);

        return "catalog";
    }

    @PostMapping("/catalog")
    public String commitBook(
            Book book,
            Model model
    ) {
        model.addAttribute("book", null);
        bookRepo.save(book);

        List<Book> catalog = bookRepo.findAll();

        model.addAttribute("catalog", catalog);

        return "catalog";

    }

    @PostMapping("/catalog/delete")
    public String deleteBook(
            @AuthenticationPrincipal User currentUser,
            @RequestParam Long id
    ) {
        bookRepo.deleteById(id);
        return "redirect:/catalog";
    }

    @GetMapping("/my-books")
    public String getOwnBooks(@AuthenticationPrincipal User user, Model model) {
        List<Book> catalog = bookRepo.findAllByOwnerId(user.getId());
        model.addAttribute("catalog", catalog);
        return "catalog";
    }

    @GetMapping("catalog/{user}")
    public String getClientBooks(@PathVariable User user, Model model){
        List<Book> catalog = bookRepo.findAllByOwnerId(user.getId());
        model.addAttribute("catalog", catalog);
        return "catalog";
    }

    @PostMapping("my-books")
    public String commitRequest(@AuthenticationPrincipal User currentUser, Book book) {
        orderService.addRequest(currentUser, book);
        return "redirect:/my-books";
    }

    @GetMapping("/order/{book_id}")
    public String createOrder(@PathVariable Long book_id, Model model) {
        Book book = bookRepo.findByIdAndInStockTrue(book_id);
        model.addAttribute("book", book);
        return "order";
    }

    @PostMapping("/order/{book_id}")
    public String commitOrder(@AuthenticationPrincipal User user,
                              @PathVariable Long book_id,
                              Model model) {
        Book book = bookRepo.findByIdAndInStockTrue(book_id);
        orderService.addOrder(user, book);

        List<Order> orders = orderService.findAllByClientId(user.getId());
        model.addAttribute("orders", orders);
        return "redirect:/my-orders";
    }

    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @PostMapping("/orders")
    public String approveOrder(@RequestParam Long order_id) {
        orderService.approveOrder(order_id);
        return "redirect:/orders";
    }

    @GetMapping("/my-orders")
    public String getClientOrders(@AuthenticationPrincipal User user, Model model) {
        List<Order> orders = orderService.findAllByClientId(user.getId());

        model.addAttribute("orders", orders);
        return "my-orders";

    }

    @PostMapping("/my-orders")
    public String confirmOrder(@AuthenticationPrincipal User currentUser, @RequestParam Long order_id) {
        orderService.confirmOrder(currentUser, order_id);
        return "redirect:/my-orders";
    }

    @GetMapping("/orders/{user_id}")
    public String getOrdersByUser(@PathVariable Long user_id, Model model) {
        List<Order> orders = orderService.findAllByClientId(user_id);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/return-requests")
    public String getAllReturnRequests(Model model) {
        List<Order> orders = orderService.findAllRequests();
        model.addAttribute("orders", orders);
        return "return-requests";
    }

    @PostMapping("/return-requests")
    public String approveReturnRequest(@RequestParam Long order_id) {
        orderService.approveRequest(order_id);
        return "redirect:/return-requests";
    }


}
