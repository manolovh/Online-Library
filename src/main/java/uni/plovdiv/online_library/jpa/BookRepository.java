package uni.plovdiv.online_library.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uni.plovdiv.online_library.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingOrAuthorContainingOrGenreContaining(String title, String author, String genre);
}
