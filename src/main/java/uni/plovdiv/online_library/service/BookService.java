package uni.plovdiv.online_library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import uni.plovdiv.online_library.dto.PopularBookDto;
import uni.plovdiv.online_library.jpa.BookRepository;
import uni.plovdiv.online_library.jpa.TakenBookRepository;
import uni.plovdiv.online_library.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final TakenBookRepository takenBookRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book existingBook = book.get();
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setGenre(updatedBook.getGenre());
            existingBook.setYear(updatedBook.getYear());
            existingBook.setAvailableCopies(updatedBook.getAvailableCopies());
            return bookRepository.save(existingBook);
        }
        throw new RuntimeException("Book not found");
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Page<Book> searchBooks(String query, Pageable pageable) {
        return bookRepository.searchByQuery(query, pageable);
    }

    public Book borrowBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            return bookRepository.save(book);
        }
        throw new RuntimeException("No copies available");
    }

    public Book returnBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        return bookRepository.save(book);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public List<PopularBookDto> getPopularBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> top10Books = takenBookRepository.findTop10MostFrequentBooks(pageable);

        List<PopularBookDto> popularBooks = new ArrayList<>();
        for (Object[] details: top10Books) {
            Long bookId = (Long) details[0];
            Optional<Book> optionalBook = bookRepository.findById(bookId);

            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                PopularBookDto popularBook = new PopularBookDto(book.getTitle(), book.getAuthor(), (Long) details[1]);
                popularBooks.add(popularBook);
            }
        }

        return popularBooks;
    }
}
