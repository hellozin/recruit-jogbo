package univ.study.recruitjogbo.member;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {

    @Id @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 4, max = 25)
    private String memberId;

    @NotBlank
    private String password;

    @NotBlank
    @Size(min = 4, max = 25)
    private String name;

    @Email
    private String email;

    @Builder
    public Member(String memberId, String password, String name, String email) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public boolean checkPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

}
