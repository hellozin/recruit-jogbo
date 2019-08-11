package univ.study.recruitjogbo.message;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class EmailConfirmRequestMessage {

    private String targetEmail;

    private String emailConfirmToken;

}
