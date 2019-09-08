package univ.study.recruitjogbo.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.RecruitType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private RecruitType recruitType;

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
    public Post(Member author, String companyName, RecruitType recruitType, LocalDate deadLine, String review) {
        this.author = author;
        this.companyName = companyName;
        this.recruitType = recruitType;
        this.deadLine = deadLine;
        this.review = review;
    }

    public void edit(String companyName, RecruitType recruitType, LocalDate deadLine, String review) {
        this.companyName = companyName;
        this.recruitType = recruitType;
        this.deadLine = deadLine;
        this.review = review;
    }

}
