package univ.study.recruitjogbo.post;

import lombok.*;
import univ.study.recruitjogbo.member.RecruitType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends AuditorEntity {

    @Id @GeneratedValue
    private Long id;

    @NotBlank
    private String companyName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RecruitType recruitType;

    @NotNull
    private LocalDate deadLine;

    @Lob
    @NotBlank
    private String review;

    @Builder
    public Post(String companyName, RecruitType recruitType, LocalDate deadLine, String review) {
        this.companyName = companyName;
        this.recruitType = recruitType;
        this.deadLine = deadLine;
        this.review = review;
    }

}
