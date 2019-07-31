package univ.study.recruitjogbo.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
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

    private final MemberService memberService;

    @Transactional
    public Post write(Long authorId,
                      @NotBlank String companyName,
                      @NotNull RecruitType recruitType,
                      @NotNull LocalDate deadLine,
                      @NotBlank String review) {
        Member author = memberService.findById(authorId)
                .orElseThrow(() -> new NotFoundException(Member.class, authorId.toString()));

        return save(new Post.PostBuilder()
                .author(author)
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
