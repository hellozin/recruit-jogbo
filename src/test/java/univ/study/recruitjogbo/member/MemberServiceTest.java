package univ.study.recruitjogbo.member;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberServiceTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MemberService memberService;

    private String userId;
    private String password;
    private String name;
    private String email;

    @BeforeAll
    void setUp() {
        userId = "hellozin";
        password = "1234";
        name = "hello";
        email = "hello@ynu.ac.kr";
    }

    @Test
    @Order(1)
    void 사용자를_추가한다() {
        Member member = memberService.join(userId, password, name, email);
        assertThat(member, is(notNullValue()));
        assertThat(member.getId(), is(notNullValue()));
        assertThat(member.getEmail(), is(email));
        log.info("Join member : {}", member);
    }

    @Test
    @Order(2)
    void 사용자_목록을_가져온다() {
        List<Member> members = memberService.findAll();
        assertThat(members, is(notNullValue()));
        assertThat(members.size(), is(1));
    }

}