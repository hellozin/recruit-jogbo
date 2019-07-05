package univ.study.recruitjogbo.content;

import lombok.Getter;
import lombok.ToString;
import univ.study.recruitjogbo.member.RecruitType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@ToString
public class Content {

    @Id @GeneratedValue
    private final Long seq;

    @NotBlank
    private final String companyName;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private final RecruitType recruitType;

    @NotNull
    private final LocalDate deadLine;

    public Content(String companyName, RecruitType recruitType, LocalDate deadLine) {
        this(null, companyName, recruitType, deadLine);
    }

    public Content(Long seq, String companyName, RecruitType recruitType, LocalDate deadLine) {
        this.seq = seq;
        this.companyName = companyName;
        this.recruitType = recruitType;
        this.deadLine = deadLine;
    }
}
