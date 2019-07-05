package univ.study.recruitjogbo.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.study.recruitjogbo.member.RecruitType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PostingRequest {

    @NotBlank
    private String companyName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RecruitType recruitType;

    @NotNull
    private LocalDate deadLine;

    @NotBlank
    private String review;

}
