package univ.study.recruitjogbo.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.study.recruitjogbo.validator.UnivEmail;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @Size(min = 4, max = 25)
    private String username;

    @Size(min = 4, max = 25)
    private String password;

    @UnivEmail
    private String email;

}
