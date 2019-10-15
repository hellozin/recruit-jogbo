package univ.study.recruitjogbo.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import univ.study.recruitjogbo.api.request.JoinRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("local")
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
        JoinRequest joinRequest = mock(JoinRequest.class);
        when(joinRequest.getUsername()).thenReturn(username);
        when(joinRequest.getPassword()).thenReturn(password);
        when(joinRequest.getEmail()).thenReturn(email);

        Member member = memberService.join(joinRequest);
        assertThat(member).isNotNull();
        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
//        log.info("Join member : {}", member);
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