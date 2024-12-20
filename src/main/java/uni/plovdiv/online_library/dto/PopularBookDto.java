package uni.plovdiv.online_library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PopularBookDto {
    private String bookName;
    private String authorName;
    private Long takenCount;
}
