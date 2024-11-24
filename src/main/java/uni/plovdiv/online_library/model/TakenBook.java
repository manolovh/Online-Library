package uni.plovdiv.online_library.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TakenBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderId;
    Long bookId;
    String takenFrom;
    Date takenAt;
    Boolean returned;

    public TakenBook(Long bookId, String takenFrom, Date takenAt) {
        this.bookId = bookId;
        this.takenFrom = takenFrom;
        this.takenAt = takenAt;
        this.returned = false;
    }
}
