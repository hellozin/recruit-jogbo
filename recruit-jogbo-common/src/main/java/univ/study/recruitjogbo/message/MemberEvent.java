package univ.study.recruitjogbo.message;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class MemberEvent {

    private Long id;

    private String username;

    private String email;

}
