package univ.study.recruitjogbo.api.response;

import lombok.*;
import univ.study.recruitjogbo.member.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class AuthenticationResult {

    private String apiToken;

    private Member member;

}
