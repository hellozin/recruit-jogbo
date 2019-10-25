package univ.study.recruitjogbo.api.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberUpdatePasswordRequest {

    @Size(min = 4, max = 25)
    @NotNull
    private String originPassword;

    @Size(min = 4, max = 25)
    @NotNull
    private String newPassword;

}
