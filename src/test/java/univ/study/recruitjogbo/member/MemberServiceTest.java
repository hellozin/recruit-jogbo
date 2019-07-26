package univ.study.recruitjogbo.member;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberServiceTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MemberService memberService;

    private String memberId;
    private String password;
    private String name;
    private String email;

    @BeforeAll
    void setUp() {
        memberId = "hellozin";
        password = "12345";
        name = "hello";
        email = "hello@ynu.ac.kr";
    }

    @Test
    @Order(1)
    void 사용자를_추가한다() {
        Member member = memberService.join(memberId, password, name, email);
        assertThat(member).isNotNull();
        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
        log.info("Join member : {}", member);
    }

    @Test
    @Order(2)
    void 추가한_사용자로_로그인한다() {
        Member loginMember = memberService.login(memberId, password);
        assertThat(loginMember.getEmail()).isEqualTo(email);
    }

    @Test
    @Order(2)
    void 사용자_목록을_가져온다() {
        List<Member> members = memberService.findAll();
        assertThat(members).isNotNull();
        assertThat(members.size()).isEqualTo(1);
    }

}