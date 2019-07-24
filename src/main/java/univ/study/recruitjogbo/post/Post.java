package univ.study.recruitjogbo.post;

import lombok.*;
import univ.study.recruitjogbo.member.RecruitType;

import javax.persistence.*;
import java.time.LocalDate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends AuditorEntity {

    @Id @GeneratedValue
    private Long id;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private RecruitType recruitType;

    private LocalDate deadLine;

    @Lob
    private String review;

    @Builder
    public Post(String companyName, RecruitType recruitType, LocalDate deadLine, String review) {
        checkArgument(!isNullOrEmpty(companyName), "Company name must be provided.");
        checkNotNull(recruitType, "Recruit type must be provided.");
        checkNotNull(deadLine, "Deadline must be provided.");
        checkArgument(!isNullOrEmpty(review), "Review must be provided.");

        this.companyName = companyName;
        this.recruitType = recruitType;
        this.deadLine = deadLine;
        this.review = review;
    }

}
