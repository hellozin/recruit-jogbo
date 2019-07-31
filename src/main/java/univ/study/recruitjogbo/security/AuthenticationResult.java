package univ.study.recruitjogbo.security;

import lombok.Getter;
import lombok.ToString;
import univ.study.recruitjogbo.member.Member;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@Getter
@ToString
public class AuthenticationResult {

    private final String apiToken;

    private final Member member;

    public AuthenticationResult(String apiToken, Member member) {
        notBlank(apiToken, "ApiToken must be provided.");
        notNull(member, "Member must be provided.");

        this.apiToken = apiToken;
        this.member = member;
    }

}
