package univ.study.recruitjogbo.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @Size(min = 4, max = 25)
    private String username;

    @Size(min = 4, max = 25)
    private String password;

    @Email
    @NotBlank
    private String email;

}
