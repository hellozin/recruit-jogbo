package univ.study.recruitjogbo.post;

import lombok.*;
import univ.study.recruitjogbo.member.RecruitType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue
    private Long seq;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private RecruitType recruitType;

    private LocalDate deadLine;

    private String review;

    @Builder
    public Post(String companyName, RecruitType recruitType, LocalDate deadLine, String review) {
        this.companyName = companyName;
        this.recruitType = recruitType;
        this.deadLine = deadLine;
        this.review = review;
    }
}
