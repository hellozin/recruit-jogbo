package univ.study.recruitjogbo;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class EmailConfirmRequest {

    private String targetEmail;

    private String emailConfirmLink;

}
