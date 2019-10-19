package univ.study.recruitjogbo.api.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AuthenticationRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String principal;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String credentials;

}
