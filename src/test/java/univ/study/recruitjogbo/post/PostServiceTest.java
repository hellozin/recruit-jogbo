package univ.study.recruitjogbo.post;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.member.RecruitType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostServiceTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    List<Post> data;

    private String companyName;
    private String recruitType;
    private LocalDate deadLine;
    private String review;
    private Member author;

    private String randomString3000;

    @BeforeAll
    void setUp() {
        randomString3000 = RandomStringUtils.randomAlphabetic(3000);

        companyName = "NAVER";
        recruitType = "RESUME";
        deadLine = LocalDate.now();
        review = randomString3000;

        author = memberService.save(
                new Member("hellozin", "password1234", "paul", "hello@gmail.com"));

        data = new ArrayList<>();
        data.add(new Post("LINE", RecruitType.RESUME, LocalDate.of(2019, 1, 1), randomString3000));
        data.add(new Post("Kakao", RecruitType.RESUME, LocalDate.of(2017, 3, 1), randomString3000));
        data.add(new Post("LINE", RecruitType.CODING, LocalDate.of(2019, 1, 25), randomString3000));
        data.add(new Post("Google", RecruitType.INTERVIEW, LocalDate.of(2019, 5, 1), randomString3000));
        data.add(new Post("LINE", RecruitType.INTERVIEW, LocalDate.of(2018, 12, 1), randomString3000));
    }

    @Test
    @Order(1)
    void 포스트를_작성한다() {
        Post post = postService.write(author.getId(), companyName, RecruitType.valueOf(recruitType), deadLine, review);

        assertThat(post).isNotNull();
        assertThat(post).isNotNull();
        assertThat(post.getRecruitType()).isEqualByComparingTo(RecruitType.RESUME);
        assertThat(post.getDeadLine()).isEqualTo(deadLine);
        log.info("Written post : {}", post);
    }

    @Test
    @Order(2)
    void 데이터_추가() {
        data.forEach(post -> {
            Post write = postService.write(author.getId(), post.getCompanyName(), post.getRecruitType(), post.getDeadLine(), post.getReview());
            log.info("Data insert {}", write);
        });
    }

    @Test
    @Order(3)
    void 전체_포스트를_조회한다() {
        List<Post> posts = postService.findAll();
        assertThat(posts).isNotNull();
        assertThat(posts.size()).isEqualTo(data.size()+1);
    }

    @Test
    @Order(4)
    void 기업명으로_포스트를_조회한다() {
        List<Post> postOfLine = postService.findByCompanyName("LINE");
        assertThat(postOfLine).isNotNull();
        assertThat(postOfLine.size()).isEqualTo(3);
    }

}