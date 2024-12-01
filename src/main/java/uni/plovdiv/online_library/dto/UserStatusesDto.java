package uni.plovdiv.online_library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserStatusesDto {
    private Long active;
    private Long inactive;
}
