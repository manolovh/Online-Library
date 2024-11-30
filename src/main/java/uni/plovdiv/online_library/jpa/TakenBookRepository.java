package uni.plovdiv.online_library.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import uni.plovdiv.online_library.model.TakenBook;

public interface TakenBookRepository extends JpaRepository<TakenBook, Long> {
    @Query("SELECT t FROM TakenBook t WHERE t.takenFrom = :takenFrom AND t.bookId = :bookId AND t.returned = false")
    Optional<TakenBook> findByTakenFromAndBookIdAndReturnedFalse(String takenFrom, Long bookId);

    @Query("SELECT t FROM TakenBook t WHERE t.takenFrom = :takenFrom and t.returned = false")
    List<TakenBook> findAllTakenByTakenFromAndReturnedFalse(@Param("takenFrom") String takenFrom);

    @Query("SELECT tb.bookId, COUNT(tb) AS borrowCount " +
        "FROM TakenBook tb " +
        "GROUP BY tb.bookId " +
        "ORDER BY borrowCount DESC")
    List<Object[]> findTop10MostFrequentBooks(Pageable pageable);
}
