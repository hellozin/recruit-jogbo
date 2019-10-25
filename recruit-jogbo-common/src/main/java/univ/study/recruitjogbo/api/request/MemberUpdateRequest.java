package univ.study.recruitjogbo.api.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import univ.study.recruitjogbo.validator.UnivEmail;

import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberUpdateRequest {

    @Size(min = 4, max = 25)
    private String username;

    @UnivEmail
    private String email;

}
