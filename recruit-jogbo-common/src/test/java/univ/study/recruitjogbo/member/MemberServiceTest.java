package univ.study.recruitjogbo.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import univ.study.recruitjogbo.api.request.JoinRequest;
import univ.study.recruitjogbo.api.request.MemberUpdateRequest;
import univ.study.recruitjogbo.error.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("local")
@Transactional
@Slf4j
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Long memberId;
    String username;
    String password;
    String email;

    @BeforeAll
    void setUp() {
        username = "username";
        password = "password";
        email = "user@ynu.ac.kr";
        memberId = memberRepository.save(new Member(username, passwordEncoder.encode(password), email)).getId();
    }

    @Test
    void 사용자를_추가한다() {
        JoinRequest joinRequest = mock(JoinRequest.class);
        when(joinRequest.getUsername()).thenReturn("newUsername");
        when(joinRequest.getPassword()).thenReturn("newPassword");
        when(joinRequest.getEmail()).thenReturn("newUser@ynu.ac.kr");
        Member member = memberService.join(joinRequest);

        assertThat(member).isNotNull();
        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(joinRequest.getEmail());
        log.info("Joined member : {}", member);
    }

    @Test
    void 추가한_사용자로_로그인한다() {
        Member loginMember = memberService.login(username, password);

        assertThat(loginMember.getEmail()).isEqualTo(email);
    }

    @Test
    void 사용자_정보를_수정한다() {
        Long memberId = memberService.findByUsername(username).map(Member::getId)
                .orElseThrow(() -> new NotFoundException(Member.class, username));
        MemberUpdateRequest memberUpdateRequest = mock(MemberUpdateRequest.class);
        when(memberUpdateRequest.getUsername()).thenReturn("newUsername");
        when(memberUpdateRequest.getEmail()).thenReturn("newUser@ynu.ac.kr");

        Member updatedMember = memberService.update(memberId, memberUpdateRequest);

        assertThat(updatedMember).isNotNull();
        assertThat(updatedMember.getUsername()).isEqualTo(memberUpdateRequest.getUsername());
        assertThat(updatedMember.getEmail()).isEqualTo(memberUpdateRequest.getEmail());
        log.info("Updated member : {}", updatedMember);
    }

    @Test
    void 사용자를_삭제한다() {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new NotFoundException(Member.class, memberId.toString()));

        memberService.delete(member.getId());

        assertThat(memberService.findById(member.getId()).isPresent()).isFalse();
    }

}