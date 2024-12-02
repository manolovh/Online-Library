package uni.plovdiv.online_library.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import uni.plovdiv.online_library.dto.PopularBookDto;
import uni.plovdiv.online_library.dto.TakenBookInfo;
import uni.plovdiv.online_library.dto.UserStatusesDto;
import uni.plovdiv.online_library.model.Book;
import uni.plovdiv.online_library.service.BookService;
import uni.plovdiv.online_library.service.UserService;

@RestController
@RequestMapping("/admin")
@Secured("ROLE_ADMIN")
@AllArgsConstructor
public class AdminController {
    private final BookService bookService;
    private final UserService userService;

    @PostMapping("/books/add")
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/books/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/books/popular")
    public List<PopularBookDto> searchBooks() {
        return bookService.getPopularBooks();
    }

    @GetMapping("/books/taken")
    public List<TakenBookInfo> takenBooks() {
        return bookService.getTakenBooks();
    }

    @GetMapping("/userStats")
    public UserStatusesDto getUserStatuses() {
        return userService.getUserStatuses();
    }

    @GetMapping("/books")
    public Page<Book> getBooks(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        Model model
    ) {
        Page<Book> booksPage = bookService.getBooks(PageRequest.of(page, size));
        model.addAttribute("booksPage", booksPage);
        return booksPage;
    }

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }
}