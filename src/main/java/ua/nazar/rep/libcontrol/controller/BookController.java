package ua.nazar.rep.libcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.service.BookService;

import javax.validation.Valid;
import java.util.Map;

//TODO Improve logic separation
@Controller
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/catalog")
    public String getBook(@RequestParam(required = false) Book book,
                          @RequestParam(required = false, defaultValue = "") String filter,
                          @RequestParam(required = false, defaultValue = "") String genreFilter,
                          @PageableDefault(sort = {"name"}, size = 25) Pageable pageable,
                          Model model) {
        Page<Book> page = bookService.findAllByFilters(filter, genreFilter, pageable);

        model.addAttribute("page", page);
        model.addAttribute("filter", filter.isEmpty() ? null : filter);
        model.addAttribute("genreFilter", genreFilter.isEmpty() ? null : genreFilter);
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

            Page<Book> page = bookService.findAll(pageable);

            model.addAttribute("book", book);
            model.addAttribute("page", page);
            return "catalog";
        } else {
            model.addAttribute("book", null);
            bookService.save(book);

            Page<Book> page = bookService.findAll(pageable);

            model.addAttribute("page", page);
        }

        return "redirect:/catalog";

    }

    @PreAuthorize("hasAuthority('ROLE_LIBRARIAN')")
    @PostMapping("/catalog/delete")
    public String deleteBook(@RequestParam Long id) {
        bookService.deleteById(id);
        return "redirect:/catalog";
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @GetMapping("/my-books")
    public String getOwnBooks(@AuthenticationPrincipal User user,
                              @RequestParam(required = false, defaultValue = "") String filter,
                              @RequestParam(required = false, defaultValue = "") String genreFilter,
                              @PageableDefault(sort = {"name"}, size = 25) Pageable pageable,
                              Model model) {
        Page<Book> page = bookService.findAllByOwnerIdAndFilters(user.getId(), filter, genreFilter, pageable);
        model.addAttribute("page", page);
        model.addAttribute("owner", user);
        model.addAttribute("filter", filter.isEmpty() ? null : filter);
        model.addAttribute("genreFilter", genreFilter.isEmpty() ? null : genreFilter);
        return "catalog";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_LIBRARIAN','ROLE_MANAGER','ROLE_DIRECTOR')")
    @GetMapping("catalog/{user}")
    public String getClientBooks(@PathVariable User user,
                                 @RequestParam(required = false, defaultValue = "") String filter,
                                 @RequestParam(required = false, defaultValue = "") String genreFilter,
                                 @PageableDefault(sort = {"name"}, size = 25) Pageable pageable,
                                 Model model) {
        Page<Book> page = bookService.findAllByOwnerIdAndFilters(user.getId(), filter, genreFilter, pageable);
        model.addAttribute("page", page);
        model.addAttribute("owner", user);
        model.addAttribute("filter", filter.isEmpty() ? null : filter);
        model.addAttribute("genreFilter", genreFilter.isEmpty() ? null : genreFilter);

        return "catalog";
    }

}
