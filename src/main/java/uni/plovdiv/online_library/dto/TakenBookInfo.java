package uni.plovdiv.online_library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TakenBookInfo {
    String title;
    String author;
    String takenFrom;
    String daysRemaining;
}
