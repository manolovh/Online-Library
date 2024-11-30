package uni.plovdiv.online_library.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uni.plovdiv.online_library.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:query% OR b.author LIKE %:query% OR b.genre LIKE %:query%")
    Page<Book> searchByQuery(@Param("query") String query, Pageable pageable);
}
