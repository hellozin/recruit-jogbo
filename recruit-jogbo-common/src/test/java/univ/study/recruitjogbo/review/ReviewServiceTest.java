package univ.study.recruitjogbo.review;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import univ.study.recruitjogbo.api.request.ReviewPublishRequest;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.review.recruitType.RecruitType;
import univ.study.recruitjogbo.review.recruitType.RecruitTypeRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("local")
@Slf4j
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RecruitTypeRepository recruitTypeRepository;

    @MockBean
    RabbitTemplate rabbitTemplate;

    private Member author1;
    private Member author2;

    final String companyName = "company";
    final LocalDate someDay = LocalDate.of(2019,1,1);
    final String review = "review";

    @BeforeAll
    void setUp() {
        author1 = memberService.save(new Member("author1", "author1", "author1@ynu.ac.kr"));
        author2 = memberService.save(new Member("author2", "author2", "author2@ynu.ac.kr"));
    }

    @Test
    @Order(1)
    void 포스트를_작성한다() {
        ReviewPublishRequest request = mock(ReviewPublishRequest.class);
        when(request.getCompanyName()).thenReturn(companyName);
        when(request.getRecruitTypes()).thenReturn(RecruitType.values());
        when(request.getDeadLine()).thenReturn(someDay);
        when(request.getReview()).thenReturn(this.review);
        Review review = reviewService.publish(author1.getId(), request);

        assertThat(review).isNotNull();
        assertThat(review.getCompanyName()).isEqualTo(companyName);
        assertThat(review.getRecruitTypes().size()).isEqualTo(RecruitType.values().length);
        assertThat(review.getDeadLine()).isEqualTo(someDay);
        assertThat(review.getReview()).isEqualTo(this.review);
//        log.info("Written review : {}", review);
    }

    @Test
    @Order(2)
    void 전체_포스트를_조회한다() {
        Page<Review> posts = reviewService.findAll(Pageable.unpaged());
        assertThat(posts).isNotEmpty();
        assertThat(posts.getTotalElements()).isEqualTo(1);
    }

    @Test
    @Order(3)
    void 기업명으로_포스트를_조회한다() {
        ReviewPublishRequest request = mock(ReviewPublishRequest.class);
        when(request.getCompanyName()).thenReturn("newCompany");
        when(request.getRecruitTypes()).thenReturn(RecruitType.values());
        when(request.getDeadLine()).thenReturn(someDay);
        when(request.getReview()).thenReturn(review);

        reviewService.publish(author1.getId(), request);
        reviewService.publish(author2.getId(), request);

        Page<Review> postOfLine = reviewService.findAll(ReviewSpecs.withCompanyName("newCompany"), Pageable.unpaged());
        assertThat(postOfLine).isNotEmpty();
        assertThat(postOfLine.getTotalElements()).isEqualTo(2);
    }

    @Test
    @Order(4)
    void RecruitType으로_포스트를_조회한다() {
        ReviewPublishRequest request = mock(ReviewPublishRequest.class);
        when(request.getCompanyName()).thenReturn(companyName);
        when(request.getRecruitTypes()).thenReturn(new RecruitType[]{RecruitType.RESUME});
        when(request.getDeadLine()).thenReturn(someDay);
        when(request.getReview()).thenReturn(review);

        reviewService.publish(author1.getId(), request);

        long total = reviewService.findAll(ReviewSpecs.withRecruitType(RecruitType.INTERVIEW), Pageable.unpaged()).getTotalElements();
        Page<Review> postWithResume = reviewService.findAll(ReviewSpecs.withRecruitType(RecruitType.RESUME), Pageable.unpaged());
        assertThat(postWithResume).isNotNull();
        assertThat(postWithResume.getTotalElements()).isEqualTo(total+1);
    }

    @Test
    @Order(5)
    void 작성자아이디로_포스트를_조회한다() {
        Member newAuthor =
                memberService.save(new Member("authorNew", "authorNew", "new@ynu.ac.kr"));

        ReviewPublishRequest request = mock(ReviewPublishRequest.class);
        when(request.getCompanyName()).thenReturn(companyName);
        when(request.getRecruitTypes()).thenReturn(RecruitType.values());
        when(request.getDeadLine()).thenReturn(someDay);
        when(request.getReview()).thenReturn(review);
        reviewService.publish(newAuthor.getId(), request);

        Page<Review> posts = reviewService.findAll(ReviewSpecs.withAuthorName(newAuthor.getUsername()), Pageable.unpaged());
        assertThat(posts.getTotalElements()).isEqualTo(1);
    }

}