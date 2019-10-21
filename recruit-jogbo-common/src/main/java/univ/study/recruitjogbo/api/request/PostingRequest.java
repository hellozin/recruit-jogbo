package univ.study.recruitjogbo.api.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import univ.study.recruitjogbo.post.recruitType.RecruitTypes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostingRequest {

    @NotBlank(message = "기업명을 입력해주세요.")
    private String companyName;

    private String companyDetail;

    @NotEmpty(message = "전형 종류를 입력해주세요.")
    private RecruitTypes[] recruitTypes;

    @NotNull(message = "날짜를 입력해주세요. 'yyyy-MM-dd'")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadLine;

    @NotBlank(message = "후기를 입력해주세요.")
    private String review;

}
