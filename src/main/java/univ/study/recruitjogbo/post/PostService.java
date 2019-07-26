package univ.study.recruitjogbo.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import univ.study.recruitjogbo.member.RecruitType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post write(@NotBlank String companyName,
                      @NotNull RecruitType recruitType,
                      @NotNull LocalDate deadLine,
                      @NotBlank String review) {
        return save(new Post.PostBuilder()
                .companyName(companyName)
                .recruitType(recruitType)
                .deadLine(deadLine)
                .review(review)
                .build()
        );
    }

    @Transactional(readOnly = true)
    public List<Post> findByCompanyName(@NotBlank String companyName) {
        return postRepository.findByCompanyNameOrderByCreatedDateDesc(companyName);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

}
