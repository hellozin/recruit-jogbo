package univ.study.recruitjogbo.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import univ.study.recruitjogbo.post.Post;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"password"})
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private boolean emailConfirmed;

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    @JsonBackReference
    private List<Post> posts;

    @Builder
    public Member(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void update(String username, String email) {
        if (username != null) {
            this.username = username;
        }
        if (email != null) {
            this.email = email;
        }
    }

    public boolean updatePassword(PasswordEncoder passwordEncoder, String originPassword, String newPassword) {
        boolean isPasswordCorrect = checkPassword(passwordEncoder, originPassword);
        if (isPasswordCorrect) {
            this.password = passwordEncoder.encode(newPassword);
        }
        return isPasswordCorrect;
    }

    public boolean checkPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public Member setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
        return this;
    }

}
