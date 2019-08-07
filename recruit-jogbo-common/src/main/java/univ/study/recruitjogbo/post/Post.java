package univ.study.recruitjogbo.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import univ.study.recruitjogbo.member.Member;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JsonBackReference
    private Member author;

    @Builder
    public Post(String companyName, RecruitType recruitType, LocalDate deadLine, String review) {
        this.companyName = companyName;
        this.recruitType = recruitType;
        this.deadLine = deadLine;
        this.review = review;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

}
