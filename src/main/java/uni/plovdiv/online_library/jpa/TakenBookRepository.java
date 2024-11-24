package uni.plovdiv.online_library.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uni.plovdiv.online_library.model.TakenBook;

public interface TakenBookRepository extends JpaRepository<TakenBook, Long> {
    @Query("SELECT t FROM TakenBook t WHERE t.takenFrom = :takenFrom AND t.bookId = :bookId AND t.returned = false")
    Optional<TakenBook> findByTakenFromAndBookIdAndReturnedFalse(String takenFrom, Long bookId);
}
