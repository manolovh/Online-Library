package uni.plovdiv.online_library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PopularBookDto {
    private String bookName;
    private String authorName;
    private Long takenCount;
}
