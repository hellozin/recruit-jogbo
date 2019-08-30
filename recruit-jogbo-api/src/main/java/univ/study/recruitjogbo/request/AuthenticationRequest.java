package univ.study.recruitjogbo.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AuthenticationRequest {

    @NotBlank
    private String principal;

    @NotBlank
    private String credentials;

}
