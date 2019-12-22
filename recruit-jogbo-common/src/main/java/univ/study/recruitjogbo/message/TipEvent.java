package univ.study.recruitjogbo.message;

import lombok.*;
import univ.study.recruitjogbo.member.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class TipEvent {

    private Long id;

    private String title;

    private Member author;

}
