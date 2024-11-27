package uni.plovdiv.online_library.model;

import uni.plovdiv.online_library.UserRole;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="users")
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @Column(unique=true, nullable=false)
    private String email;
    
    @Column(unique=true, nullable=false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Date lastActiveAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }
}

