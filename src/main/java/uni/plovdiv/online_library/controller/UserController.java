package uni.plovdiv.online_library.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import uni.plovdiv.online_library.dto.EnrichedTakenBookDto;
import uni.plovdiv.online_library.jpa.TakenBookRepository;
import uni.plovdiv.online_library.model.Book;
import uni.plovdiv.online_library.model.TakenBook;
import uni.plovdiv.online_library.service.BookService;

@RestController
@RequestMapping("/user/books")
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class UserController {
    private final BookService bookService;
    private final TakenBookRepository takenBookRepository;

    @GetMapping("/getAllBorrowed")
    public List<EnrichedTakenBookDto> getAllBorrowed() {
        //refactor
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TakenBook> takenBooks = takenBookRepository.findAllTakenByTakenFromAndReturnedFalse(username);
        List<EnrichedTakenBookDto> bookDtos = new ArrayList<>();

        for (TakenBook tb: takenBooks) {
            Book b = bookService.getBook(tb.getBookId());
            EnrichedTakenBookDto bookDto = new EnrichedTakenBookDto();
            bookDto.setId(b.getId());
            bookDto.setReturned(tb.getReturned());
            bookDto.setTitle(b.getTitle());
            bookDto.setAuthor(b.getAuthor());
            bookDto.setTakenAt(tb.getTakenAt());
            bookDtos.add(bookDto);
        }
        return bookDtos;
    }
    @GetMapping("/search")
    public Page<Book> searchBooks(@RequestParam(name="query") String query,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "10") int size, Model model) {
        Page<Book> booksPage = bookService.searchBooks(query, PageRequest.of(page, size));
        model.addAttribute("booksPage", booksPage);  // Add paginated result
        model.addAttribute("query", query);          // Add query to keep it in the form input
        return booksPage;
    }

    @PostMapping("/borrow/{id}")
    public String borrowBook(@PathVariable Long id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (takenBookRepository.findByTakenFromAndBookIdAndReturnedFalse(username, id).isPresent()) {
            throw new IllegalStateException("You cannot take more than one copy of the same book!");
        }

        bookService.borrowBook(id);

        TakenBook takenBook = new TakenBook(id, username, new Date());
        takenBookRepository.save(takenBook);

        return "Book sucessfully borrowed!";
    }

    @GetMapping("/borrow/{id}/check")
    public Boolean isEligibleForBorrow(@PathVariable Long id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return takenBookRepository.findByTakenFromAndBookIdAndReturnedFalse(username, id).isEmpty();
    }

    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<TakenBook> takenBookOptional = takenBookRepository.findByTakenFromAndBookIdAndReturnedFalse(username, id);
        if (takenBookOptional.isEmpty()) {
            return "You don't have to return this book!";
        }

        TakenBook takenBook = takenBookOptional.get();
        takenBook.setReturned(true);
        takenBookRepository.save(takenBook);

        bookService.returnBook(id);
        
        return "Book sucessfully returned!";
    }
}