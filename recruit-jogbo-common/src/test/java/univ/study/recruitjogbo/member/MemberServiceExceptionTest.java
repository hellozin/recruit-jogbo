package univ.study.recruitjogbo.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import univ.study.recruitjogbo.api.request.JoinRequest;
import univ.study.recruitjogbo.error.DuplicatedException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("local")
@Transactional
@Slf4j
public class MemberServiceExceptionTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    RabbitTemplate rabbitTemplate;

    JoinRequest joinRequest = mock(JoinRequest.class);

    @BeforeAll
    void setUp() {
        when(joinRequest.getUsername()).thenReturn("username");
        when(joinRequest.getPassword()).thenReturn("password");
        when(joinRequest.getEmail()).thenReturn("user@ynu.ac.kr");
    }

    @Test
    void 사용자추가시_아이디중복을_검증한다() {
        Member member = memberService.join(joinRequest);

        JoinRequest usernameDuplicateRequest = mock(JoinRequest.class);
        when(usernameDuplicateRequest.getUsername()).thenReturn(member.getUsername());
        when(usernameDuplicateRequest.getPassword()).thenReturn(member.getPassword());
        when(usernameDuplicateRequest.getEmail()).thenReturn("another@ynu.ac.kr");

        assertThrows(DuplicatedException.class, () -> memberService.join(usernameDuplicateRequest));
    }

    @Test
    void 사용자추가시_이메일중복을_검증한다() {
        Member member = memberService.join(joinRequest);

        JoinRequest emailDuplicateRequest = mock(JoinRequest.class);
        when(emailDuplicateRequest.getUsername()).thenReturn("anotherUsername");
        when(emailDuplicateRequest.getPassword()).thenReturn(member.getPassword());
        when(emailDuplicateRequest.getEmail()).thenReturn(member.getEmail());

        assertThrows(DuplicatedException.class, () -> memberService.join(emailDuplicateRequest));
    }

}
