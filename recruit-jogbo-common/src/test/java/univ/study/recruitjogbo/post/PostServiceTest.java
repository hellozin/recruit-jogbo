package univ.study.recruitjogbo.post;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import univ.study.recruitjogbo.api.request.PostingRequest;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("local")
@Slf4j
class PostServiceTest {

    @Autowired
    private PostService postService;

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
        PostingRequest request = new PostingRequest();
        request.setCompanyName(companyName);
        request.setRecruitTypes(RecruitTypes.values());
        request.setDeadLine(someDay);
        request.setReview(review);
        Post post = postService.write(author1.getId(), request);

        assertThat(post).isNotNull();
        assertThat(post.getCompanyName()).isEqualTo(companyName);
        assertThat(post.getRecruitTypes().size()).isEqualTo(RecruitTypes.values().length);
        assertThat(post.getDeadLine()).isEqualTo(someDay);
        assertThat(post.getReview()).isEqualTo(review);
//        log.info("Written post : {}", post);
    }

    @Test
    @Order(2)
    void 전체_포스트를_조회한다() {
        Page<Post> posts = postService.findAll(Pageable.unpaged());
        assertThat(posts).isNotEmpty();
        assertThat(posts.getTotalElements()).isEqualTo(1);
    }

    @Test
    @Order(3)
    void 기업명으로_포스트를_조회한다() {
        PostingRequest request = new PostingRequest();
        request.setRecruitTypes(RecruitTypes.values());
        request.setDeadLine(someDay);
        request.setReview(review);

        request.setCompanyName("newCompany");

        postService.write(author1.getId(), request);
        postService.write(author2.getId(), request);

        Page<Post> postOfLine = postService.findAll(PostSpecs.withCompanyName("newCompany"), Pageable.unpaged());
        assertThat(postOfLine).isNotEmpty();
        assertThat(postOfLine.getTotalElements()).isEqualTo(2);
    }

    @Test
    @Order(4)
    void RecruitType으로_포스트를_조회한다() {
        PostingRequest request = new PostingRequest();
        request.setCompanyName(companyName);
        request.setDeadLine(someDay);
        request.setReview(review);

        request.setRecruitTypes(new RecruitTypes[]{RecruitTypes.RESUME});

        postService.write(author1.getId(), request);

        long total = postService.findAll(PostSpecs.withRecruitType(RecruitTypes.INTERVIEW), Pageable.unpaged()).getTotalElements();
        Page<Post> postWithResume = postService.findAll(PostSpecs.withRecruitType(RecruitTypes.RESUME), Pageable.unpaged());
        assertThat(postWithResume).isNotNull();
        assertThat(postWithResume.getTotalElements()).isEqualTo(total+1);
    }

    @Test
    @Order(5)
    void 작성자아이디로_포스트를_조회한다() {
        Member newAuthor =
                memberService.save(new Member("authorNew", "authorNew", "new@ynu.ac.kr"));

        PostingRequest request = new PostingRequest();
        request.setCompanyName(companyName);
        request.setRecruitTypes(RecruitTypes.values());
        request.setDeadLine(someDay);
        request.setReview(review);
        postService.write(newAuthor.getId(), request);

        Page<Post> posts = postService.findAll(PostSpecs.withAuthorName(newAuthor.getUsername()), Pageable.unpaged());
        assertThat(posts.getTotalElements()).isEqualTo(1);
    }

}