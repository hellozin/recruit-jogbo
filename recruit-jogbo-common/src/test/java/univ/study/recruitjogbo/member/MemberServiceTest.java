package univ.study.recruitjogbo.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import univ.study.recruitjogbo.request.JoinRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    RabbitTemplate rabbitTemplate;

    private String username;
    private String password;
    private String email;

    @BeforeAll
    void setUp() {
        username = "hellozin";
        password = "12345";
        email = "hello@ynu.ac.kr";
    }

    @Test
    @Order(1)
    void 사용자를_추가한다() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUsername(username);
        joinRequest.setPassword(password);
        joinRequest.setEmail(email);

        Member member = memberService.join(joinRequest);
        assertThat(member).isNotNull();
        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
        log.info("Join member : {}", member);
    }

    @Test
    @Order(2)
    void 추가한_사용자로_로그인한다() {
        Member loginMember = memberService.login(username, password);
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