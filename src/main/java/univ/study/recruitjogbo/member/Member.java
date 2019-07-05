package univ.study.recruitjogbo.member;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {

    @Id @GeneratedValue
    private Long seq;

    @NotBlank
    @Length(min = 4)
    private String userId;

    @NotBlank
    @Length(min = 4, max = 15)
    private String password;

    @NotBlank
    private String name;

    @Email
    private String email;

    @Builder
    public Member(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

}
