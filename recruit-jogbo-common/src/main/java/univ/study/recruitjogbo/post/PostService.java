package univ.study.recruitjogbo.post;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Validated
public class PostService {

    private final PostRepository postRepository;

    private final RecruitTypeRepository recruitTypeRepository;

    private final MemberService memberService;

    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Post write(@NotNull Long authorId,
                      @NotBlank String companyName,
                      @NotNull Set<RecruitTypes> recruitTypes,
                      @NotNull LocalDate deadLine,
                      @NotBlank String review) {
        Member author = memberService.findById(authorId)
                .orElseThrow(() -> new NotFoundException(Member.class, authorId.toString()));

        Set<RecruitType> byRecruitTypeIn = recruitTypeRepository.findByRecruitTypeIn(recruitTypes);
        Post post = save(new Post.PostBuilder()
                .author(author)
                .companyName(companyName)
                .recruitTypes(byRecruitTypeIn)
                .deadLine(deadLine)
                .review(review)
                .build());

//        rabbitTemplate.convertAndSend("post", "post.create", post);

        return post;
    }

    @Transactional
    public Post edit(@NotNull Long postId,
                     @NotBlank String companyName,
                     @NotNull Set<RecruitTypes> recruitTypes,
                     @NotNull LocalDate deadLine,
                     @NotBlank String review) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId.toString()));

        Set<RecruitType> byRecruitTypeIn = recruitTypeRepository.findByRecruitTypeIn(recruitTypes);
        post.edit(companyName, byRecruitTypeIn, deadLine, review);
        Post modifiedPost = save(post);

        rabbitTemplate.convertAndSend("post", "post.edit", modifiedPost);

        return modifiedPost;
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
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
