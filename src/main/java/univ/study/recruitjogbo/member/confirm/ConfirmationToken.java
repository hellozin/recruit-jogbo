package univ.study.recruitjogbo.member.confirm;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmationToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String userEmail;

    @NotEmpty
    private String confirmationToken;

    public ConfirmationToken(String userEmail) {
        this.userEmail = userEmail;
        this.confirmationToken = UUID.randomUUID().toString();
    }

}
