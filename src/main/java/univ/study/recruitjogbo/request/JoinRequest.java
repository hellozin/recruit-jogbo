package univ.study.recruitjogbo.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank
    @Length(min = 4)
    private String userId;

    @NotBlank
    @Length(min = 4, max = 15)
    private String password;

    @NotBlank
    private String name;

    @Email
    private String email;

}
