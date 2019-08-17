package univ.study.recruitjogbo.post;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
@Validated
public class PostService {

    private final PostRepository postRepository;

    private final MemberService memberService;

    @Transactional
    public Post write(@NotNull Long authorId,
                      @NotBlank String companyName,
                      @NotNull RecruitType recruitType,
                      @NotNull LocalDate deadLine,
                      @NotBlank String review) {
        Member author = memberService.findById(authorId)
                .orElseThrow(() -> new NotFoundException(Member.class, authorId.toString()));

        Post post = new Post.PostBuilder()
                .companyName(companyName)
                .recruitType(recruitType)
                .deadLine(deadLine)
                .review(review)
                .build();

        author.addPost(post);
        return save(post);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Post> findAll(Specification<Post> specification, Pageable pageable) {
        return postRepository.findAll(specification, pageable);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

}
