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

    @NotBlank
    @Size(min = 4)
    private String userId;

    @NotBlank
    @Size(min = 4, max = 15)
    private String password;

    @NotBlank
    private String name;

    @Email
    private String email;

}
