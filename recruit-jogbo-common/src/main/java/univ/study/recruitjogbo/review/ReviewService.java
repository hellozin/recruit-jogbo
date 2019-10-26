package univ.study.recruitjogbo.review;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import univ.study.recruitjogbo.api.request.ReviewPublishRequest;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.message.ReviewEvent;
import univ.study.recruitjogbo.message.RabbitMQ;
import univ.study.recruitjogbo.review.recruitType.RecruitType;
import univ.study.recruitjogbo.review.recruitType.RecruitTypeRepository;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final RecruitTypeRepository recruitTypeRepository;

    private final MemberService memberService;

    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Review publish(@NotNull Long authorId, ReviewPublishRequest request) {
        Member author = memberService.findById(authorId)
                .orElseThrow(() -> new NotFoundException(Member.class, authorId.toString()));

        List<RecruitType> byRecruitTypeIn = recruitTypeRepository.findByRecruitTypeIn(Arrays.asList(request.getRecruitTypes()));
        Review review = save(new Review.ReviewBuilder()
                .author(author)
                .companyName(request.getCompanyName())
                .companyDetail(request.getCompanyDetail())
                .recruitTypes(byRecruitTypeIn)
                .deadLine(request.getDeadLine())
                .review(request.getReview())
                .build());

        try {
            rabbitTemplate.convertAndSend(RabbitMQ.EXCHANGE, RabbitMQ.REVIEW_CREATE,
                    new ReviewEvent(review.getId(), review.getCompanyName(), review.getRecruitTypesEnum()));
        } catch (AmqpException exception) {
            log.warn("Fail MQ send review publish message. {}", exception.getMessage());
        }
        return review;
    }

    @Transactional
    public Review edit(@NotNull Long reviewId, ReviewPublishRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Review.class, reviewId.toString()));

        List<RecruitType> byRecruitTypeIn = recruitTypeRepository.findByRecruitTypeIn(Arrays.asList(request.getRecruitTypes()));
        review.edit(request.getCompanyName(), request.getCompanyDetail(), byRecruitTypeIn, request.getDeadLine(), request.getReview());

        try {
            rabbitTemplate.convertAndSend(RabbitMQ.EXCHANGE, RabbitMQ.REVIEW_UPDATE,
                    new ReviewEvent(review.getId(), review.getCompanyName(), review.getRecruitTypesEnum()));
        } catch (AmqpException exception) {
            log.warn("Fail MQ send review edit message. {}", exception.getMessage());
        }

        return save(review);
    }

    @Transactional
    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Review.class, reviewId.toString()));
        reviewRepository.delete(review);
    }

    @Transactional
    public void deleteByAuthorId(Long authorId) {
        String authorName = memberService.findById(authorId).map(Member::getUsername)
                .orElseThrow(() -> new NotFoundException(Member.class, authorId.toString()));
        List<Review> postsByAuthorId = reviewRepository.findAll(ReviewSpecs.withAuthorName(authorName));
        reviewRepository.deleteAll(postsByAuthorId);
    }

    @Transactional(readOnly = true)
    public Optional<Review> findById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    @Transactional(readOnly = true)
    public Page<Review> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Review> findAll(Specification<Review> specification, Pageable pageable) {
        return reviewRepository.findAll(specification, pageable);
    }

    @Transactional
    protected Review save(Review review) {
        return reviewRepository.save(review);
    }

}
