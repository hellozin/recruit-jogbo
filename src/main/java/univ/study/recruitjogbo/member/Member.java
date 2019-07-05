package univ.study.recruitjogbo.member;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@ToString
public class Member {

    @Id @GeneratedValue
    private final Long seq;

    @NotBlank
    @Length(min = 4)
    private final String userId;

    @NotBlank
    @Length(min = 4, max = 15)
    private final String password;

    @NotBlank
    private final String name;

    @Email
    private final String email;

    public Member(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public Member(Long seq, String userId, String password, String name, String email) {
        this.seq = seq;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

}
