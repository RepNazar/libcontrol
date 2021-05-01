package ua.nazar.rep.libcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nazar.rep.libcontrol.domain.Book;
import ua.nazar.rep.libcontrol.domain.Order;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.repo.BookRepo;
import ua.nazar.rep.libcontrol.service.OrderService;

import javax.validation.Valid;
import java.util.Map;

//TODO Try to separate controllers
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
    public String getBook(@RequestParam(required = false) Book book,
                          @RequestParam(required = false, defaultValue = "") String filter,
                          @PageableDefault(sort = {"name"}, size = 25) Pageable pageable,
                          Model model) {
        Page<Book> page = bookRepo.findAllByNameContains(filter ,pageable);

        model.addAttribute("page", page);
        model.addAttribute("filter", filter);
        model.addAttribute("book", book);

        return "catalog";
    }

    @PreAuthorize("hasAuthority('ROLE_LIBRARIAN')")
    @PostMapping("/catalog")
    public String commitBook(@Valid Book book,
                             BindingResult bindingResult,
                             @PageableDefault(sort = {"name"}, size = 25) Pageable pageable,
                             Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);

            Page<Book> page = bookRepo.findAll(pageable);

            model.addAttribute("book", book);
            model.addAttribute("page", page);
            return "catalog";
        } else {
            model.addAttribute("book", null);
            bookRepo.save(book);

            Page<Book> page = bookRepo.findAll(pageable);

            model.addAttribute("page", page);
        }

        return "redirect:/catalog";

    }

    @PreAuthorize("hasAuthority('ROLE_LIBRARIAN')")
    @PostMapping("/catalog/delete")
    public String deleteBook(@RequestParam Long id) {
        bookRepo.deleteById(id);
        return "redirect:/catalog";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/my-books")
    public String getOwnBooks(@AuthenticationPrincipal User user,
                              @RequestParam(required = false, defaultValue = "") String filter,
                              @PageableDefault(sort = {"name"}, size = 25) Pageable pageable,
                              Model model) {
        Page<Book> page = bookRepo.findAllByOwnerIdAndNameContains(user.getId(), filter, pageable);
        model.addAttribute("page", page);
        model.addAttribute("personalized", true);
        model.addAttribute("filter", filter);
        return "catalog";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_LIBRARIAN','ROLE_MANAGER','ROLE_DIRECTOR')")
    @GetMapping("catalog/{user}")
    public String getClientBooks(@PathVariable User user,
                                 @RequestParam(required = false, defaultValue = "") String filter,
                                 @PageableDefault(sort = {"name"}, size = 25) Pageable pageable,
                                 Model model) {
        Page<Book> page = bookRepo.findAllByOwnerIdAndNameContains(user.getId(), filter, pageable);
        model.addAttribute("page", page);
        model.addAttribute("personalized", true);
        model.addAttribute("filter", filter);

        return "catalog";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("my-books")
    public String commitRequest(@AuthenticationPrincipal User currentUser, Book book) {
        orderService.addRequest(currentUser, book);
        return "redirect:/my-books";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/order/{book_id}")
    public String createOrder(@PathVariable Long book_id, Model model) {
        Book book = bookRepo.findByIdAndInStockTrue(book_id);
        model.addAttribute("book", book);
        return "order";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/order/{book_id}")
    public String commitOrder(@AuthenticationPrincipal User user,
                              @PathVariable Long book_id,
                              @PageableDefault(sort = {"finished", "approved", "date"}, size = 25) Pageable pageable,
                              Model model) {
        Book book = bookRepo.findByIdAndInStockTrue(book_id);
        orderService.addOrder(user, book);

        Page<Order> page = orderService.findAllByClientId(user.getId(), pageable);
        model.addAttribute("page", page);
        return "redirect:/my-orders";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_DIRECTOR')")
    @GetMapping("/orders")
    public String getAllOrders(@PageableDefault(sort = {"finished", "approved", "date"}, direction = Sort.Direction.DESC, size = 25) Pageable pageable,
            Model model) {
        Page<Order> page = orderService.findAllOrders(pageable);
        model.addAttribute("page", page);
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping("/orders")
    public String approveOrder(@RequestParam Long order_id) {
        orderService.approveOrder(order_id);
        return "redirect:/orders";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/my-orders")
    public String getClientOrders(@AuthenticationPrincipal User user,
                                  @PageableDefault(sort = {"date"}, direction = Sort.Direction.DESC, size = 25) Pageable pageable,
                                  Model model) {
        Page<Order> page = orderService.findAllByClientId(user.getId(), pageable);

        model.addAttribute("page", page);
        model.addAttribute("personalized", true);
        return "my-orders";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/my-orders")
    public String confirmOrder(@AuthenticationPrincipal User currentUser, @RequestParam Long order_id) {
        orderService.confirmOrder(currentUser, order_id);
        return "redirect:/my-orders";
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/orders/{user_id}")
    public String getOrdersByUser(@PathVariable Long user_id,
                                  @PageableDefault(sort = {"finished", "approved", "date"}, size = 25) Pageable pageable,
                                  Model model) {
        Page<Order> page = orderService.findAllByClientId(user_id, pageable);
        model.addAttribute("personalized", true);
        model.addAttribute("page", page);
        return "orders";
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/return-requests")
    public String getAllReturnRequests(@PageableDefault(sort = {"finished", "approved", "date"}, size = 25) Pageable pageable, Model model) {
        Page<Order> page = orderService.findAllRequests(pageable);
        model.addAttribute("page", page);
        return "return-requests";
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping("/return-requests")
    public String approveReturnRequest(@RequestParam Long order_id) {
        orderService.approveRequest(order_id);
        return "redirect:/return-requests";
    }


}
