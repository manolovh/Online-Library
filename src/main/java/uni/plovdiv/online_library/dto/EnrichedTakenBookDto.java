package uni.plovdiv.online_library.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EnrichedTakenBookDto {
    private Long id;
    private Date takenAt;
    private Boolean returned;
    private String title;
    private String author;
}
