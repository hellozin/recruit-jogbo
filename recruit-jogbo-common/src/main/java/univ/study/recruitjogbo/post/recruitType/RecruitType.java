package univ.study.recruitjogbo.post.recruitType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitType {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecruitTypes recruitType;

    public RecruitType(RecruitTypes recruitType) {
        this.recruitType = recruitType;
    }

}

