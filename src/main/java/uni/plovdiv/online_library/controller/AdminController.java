package uni.plovdiv.online_library.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uni.plovdiv.online_library.model.Book;
import uni.plovdiv.online_library.service.BookService;

@RestController
@RequestMapping("/admin/books")
@Secured("ROLE_ADMIN")
public class AdminController {
    private final BookService bookService;

    public AdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/add")
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}