package univ.study.recruitjogbo.review.recruitType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitTypeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecruitType recruitType;

    public RecruitTypeEntity(RecruitType recruitType) {
        this.recruitType = recruitType;
    }

}


