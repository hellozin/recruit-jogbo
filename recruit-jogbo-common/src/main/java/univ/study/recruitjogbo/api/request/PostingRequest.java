package univ.study.recruitjogbo.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import univ.study.recruitjogbo.post.RecruitTypes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostingRequest {

    @NotBlank
    private String companyName;

    @NotEmpty
    private List<RecruitTypes> recruitTypes;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadLine;

    @NotBlank
    private String review;

}
