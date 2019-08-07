package univ.study.recruitjogbo.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import univ.study.recruitjogbo.member.Member;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class AuthenticationResult {

    private String apiToken;

    private Member member;

    public AuthenticationResult(String apiToken, Member member) {
        notBlank(apiToken, "ApiToken must be provided.");
        notNull(member, "Member must be provided.");

        this.apiToken = apiToken;
        this.member = member;
    }

}
