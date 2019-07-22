package univ.study.recruitjogbo.post;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import univ.study.recruitjogbo.member.RecruitType;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = {"spring.config.location=classpath:/google.yml,classpath:/mail.yml"})
class PostServiceTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PostService postService;

    private String companyName;
    private String recruitType;
    private LocalDate deadLine;
    private String review;

    @BeforeAll
    void setUp() {
        companyName = "hellozin";
        recruitType = "RESUME";
        deadLine = LocalDate.now();
        review = "review";
    }

    @Test
    @Order(1)
    void 포스트를_작성한다() {
        Post post = postService.write(companyName, RecruitType.valueOf(recruitType), deadLine, review);
        assertThat(post, is(notNullValue()));
        assertThat(post.getSeq(), is(notNullValue()));
        assertThat(post.getRecruitType(), is(RecruitType.RESUME));
        assertThat(post.getDeadLine(), is(deadLine));
        log.info("Written post : {}", post);
    }

    @Test
    @Order(2)
    void 사용자_목록을_가져온다() {
        List<Post> posts = postService.findAll();
        assertThat(posts, is(notNullValue()));
        assertThat(posts.size(), is(1));
    }

}