package univ.study.recruitjogbo.post;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import univ.study.recruitjogbo.api.request.PostingRequest;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.message.PostEvent;
import univ.study.recruitjogbo.message.RabbitMQ;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
public class PostService {

    private final PostRepository postRepository;

    private final RecruitTypeRepository recruitTypeRepository;

    private final MemberService memberService;

    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Post write(@NotNull Long authorId, PostingRequest request) {
        Member author = memberService.findById(authorId)
                .orElseThrow(() -> new NotFoundException(Member.class, authorId.toString()));

        List<RecruitType> byRecruitTypeIn = recruitTypeRepository.findByRecruitTypeIn(Arrays.asList(request.getRecruitTypes()));
        Post post = save(new Post.PostBuilder()
                .author(author)
                .companyName(request.getCompanyName())
                .recruitTypes(byRecruitTypeIn)
                .deadLine(request.getDeadLine())
                .review(request.getReview())
                .build());

        rabbitTemplate.convertAndSend(
                RabbitMQ.EXCHANGE,
                RabbitMQ.POST_CREATE,
                new PostEvent(post.getId(), post.getCompanyName(), post.getRecruitTypesEnum())
        );
        return post;
    }

    @Transactional
    public Post edit(@NotNull Long postId, PostingRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId.toString()));

        List<RecruitType> byRecruitTypeIn = recruitTypeRepository.findByRecruitTypeIn(Arrays.asList(request.getRecruitTypes()));
        post.edit(request.getCompanyName(), byRecruitTypeIn, request.getDeadLine(), request.getReview());
        return save(post);
    }

    @Transactional
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Post> findAll(Specification<Post> specification, Pageable pageable) {
        return postRepository.findAll(specification, pageable);
    }

    @Transactional
    protected Post save(Post post) {
        return postRepository.save(post);
    }

}
