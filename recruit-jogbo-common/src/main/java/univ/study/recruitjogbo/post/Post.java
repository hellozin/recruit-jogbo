package univ.study.recruitjogbo.post;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import univ.study.recruitjogbo.member.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RecruitType> recruitTypes;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadLine;

    @Lob
    private String review;

    @ManyToOne
    private Member author;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Post(Member author, String companyName, List<RecruitType> recruitTypes, LocalDate deadLine, String review) {
        this.author = author;
        this.companyName = companyName;
        this.recruitTypes = recruitTypes;
        this.deadLine = deadLine;
        this.review = review;
    }

    public void edit(String companyName, List<RecruitType> recruitTypes, LocalDate deadLine, String review) {
        this.companyName = companyName;
        this.recruitTypes = recruitTypes;
        this.deadLine = deadLine;
        this.review = review;
    }

    public List<RecruitTypes> getRecruitTypesEnum() {
        List<RecruitTypes> recruitTypesList = new ArrayList<>();
        for (RecruitType recruitType : recruitTypes) {
            recruitTypesList.add(recruitType.getRecruitType());
        }
        return recruitTypesList;
    }

}
