package univ.study.recruitjogbo.api.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import univ.study.recruitjogbo.validator.UnivEmail;

import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JoinRequest {

    @Size(min = 4, max = 25)
    private String username;

    @Size(min = 4, max = 25)
    private String password;

    @UnivEmail
    private String email;

    private String confirmUrl;

}
