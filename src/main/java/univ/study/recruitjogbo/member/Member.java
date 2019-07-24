package univ.study.recruitjogbo.member;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.regex.Pattern.matches;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String userId;

    private String password;

    private String name;

    private String email;

    @Builder
    public Member(String userId, String password, String name, String email) {
        checkArgument(!isNullOrEmpty(userId), "User id must be provided.");
        checkArgument(!isNullOrEmpty(password), "Password must be provided.");
        checkArgument(!isNullOrEmpty(name), "Name must be provided.");
        checkArgument(!isNullOrEmpty(email), "Email must be provided.");
        checkArgument(isEmail(email), "Invalid email pattern.");

        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private static boolean isEmail(String email) {
        return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", email);
    }

}
