package univ.study.recruitjogbo.member;

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
import univ.study.recruitjogbo.api.request.MemberUpdateRequest;
import univ.study.recruitjogbo.error.DuplicatedException;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.error.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("local")
@Transactional
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

    @Test
    void 사용자수정시_아이디중복을_검증한다() {
        Member member = memberService.join(joinRequest);
        MemberUpdateRequest memberUpdateRequest = mock(MemberUpdateRequest.class);
        when(memberUpdateRequest.getUsername()).thenReturn(member.getUsername());

        assertThrows(DuplicatedException.class, () -> memberService.update(member.getId(), memberUpdateRequest));
    }

    @Test
    void 사용자수정시_이메일중복을_검증한다() {
        Member member = memberService.join(joinRequest);
        MemberUpdateRequest memberUpdateRequest = mock(MemberUpdateRequest.class);
        when(memberUpdateRequest.getEmail()).thenReturn(member.getEmail());

        assertThrows(DuplicatedException.class, () -> memberService.update(member.getId(), memberUpdateRequest));
    }

    @Test
    void 로그인이_유효한지_검증한다() {
        Member member = memberService.join(joinRequest);

        assertThrows(NotFoundException.class, () ->
                memberService.login("NotFoundUsername", member.getPassword()));
        assertThrows(UnauthorizedException.class, () ->
                memberService.login(member.getUsername(), "UnauthorizedPassword"));
    }

}
