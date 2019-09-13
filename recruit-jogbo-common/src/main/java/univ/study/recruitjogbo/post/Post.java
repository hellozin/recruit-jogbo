package univ.study.recruitjogbo.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import univ.study.recruitjogbo.member.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<RecruitType> recruitTypes;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadLine;

    @Lob
    private String review;

    @ManyToOne
    @JsonBackReference
    private Member author;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Post(Member author, String companyName, Set<RecruitType> recruitTypes, LocalDate deadLine, String review) {
        this.author = author;
        this.companyName = companyName;
        this.recruitTypes = recruitTypes;
        this.deadLine = deadLine;
        this.review = review;
    }

    public void edit(String companyName, Set<RecruitType> recruitTypes, LocalDate deadLine, String review) {
        this.companyName = companyName;
        this.recruitTypes = recruitTypes;
        this.deadLine = deadLine;
        this.review = review;
    }

    public boolean isRecruitTypeMatch(RecruitTypes recruitType) {
        for (RecruitType type : recruitTypes) {
            if (type.getRecruitType().equals(recruitType)) {
                return true;
            }
        }
        return false;
    }

}
