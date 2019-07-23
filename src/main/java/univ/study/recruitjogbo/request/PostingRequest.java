package univ.study.recruitjogbo.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.study.recruitjogbo.member.RecruitType;
import univ.study.recruitjogbo.validator.Enum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PostingRequest {

    @NotBlank
    private String companyName;

    @Enum(enumClass = RecruitType.class, ignoreCase = true)
    private String recruitType;

    @NotNull
    private LocalDate deadLine;

    @NotBlank
    private String review;

    public RecruitType getRecruitType() {
        return RecruitType.valueOf(recruitType);
    }
}
