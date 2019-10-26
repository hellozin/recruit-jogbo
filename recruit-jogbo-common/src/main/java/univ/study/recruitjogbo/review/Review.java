package univ.study.recruitjogbo.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.review.recruitType.RecruitTypeEntity;
import univ.study.recruitjogbo.util.EnumValue;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private String companyDetail;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<RecruitTypeEntity> recruitTypes;

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
    public Review(Member author, String companyName, String companyDetail, List<RecruitTypeEntity> recruitTypes, LocalDate deadLine, String review) {
        this.author = author;
        this.companyName = companyName;
        this.companyDetail = companyDetail;
        this.recruitTypes = recruitTypes;
        this.deadLine = deadLine;
        this.review = review;
    }

    public void edit(String companyName, String companyDetail, List<RecruitTypeEntity> recruitTypeEntities, LocalDate deadLine, String review) {
        this.companyName = companyName;
        this.companyDetail = companyDetail;
        this.recruitTypes = recruitTypeEntities;
        this.deadLine = deadLine;
        this.review = review;
    }

    public List<EnumValue> getRecruitTypesEnum() {
        List<EnumValue> recruitTypesList = new ArrayList<>();
        for (RecruitTypeEntity recruitTypeEntity : recruitTypes) {
            recruitTypesList.add(new EnumValue(recruitTypeEntity.getRecruitType()));
        }
        return recruitTypesList;
    }

}
