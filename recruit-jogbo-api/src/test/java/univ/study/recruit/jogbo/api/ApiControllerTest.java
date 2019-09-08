package univ.study.recruit.jogbo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import univ.study.recruitjogbo.RecruitJogboApi;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.RecruitType;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.request.JoinRequest;
import univ.study.recruitjogbo.request.PostingRequest;
import univ.study.recruitjogbo.security.AuthenticationRequest;
import univ.study.recruitjogbo.security.AuthenticationResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = RecruitJogboApi.class,
        properties = "spring.config.location=" +
        "classpath:/application.yml" +
        ",classpath:/secret-jwt.yml"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("local")
@AutoConfigureMockMvc
public class ApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RabbitTemplate rabbitTemplate;

    private Member member;
    private Post post;

    private String APITOKEN;

    @BeforeAll
    void setUp() {
        member = new Member("hellozin", "hello1234", "hello@yu.ac.kr");
        post = new Post(member, "LINE",RecruitType.RESUME,LocalDate.of(2019,1,1),"Something New");
    }

    @Test
    @Order(1)
    void 멤버를_추가한다() throws Exception {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUsername(member.getUsername());
        joinRequest.setPassword(member.getPassword());
        joinRequest.setEmail(member.getEmail());

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(joinRequest))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    @Order(2)
    void 멤버_인증토큰을_발행한다() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(member.getUsername(), member.getPassword());

        MvcResult mvcResult = mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AuthenticationResult result = objectMapper.readValue(contentAsString, AuthenticationResult.class);
        APITOKEN = result.getApiToken();
    }

    @Test
    @Order(3)
    void 포스트_목록을_가져온다() throws Exception {
        mockMvc.perform(get("/api/post/list")
                .header("api_key", "Bearer " + APITOKEN)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void 새_포스트를_작성한다() throws Exception {
        PostingRequest request = new PostingRequest();
        request.setCompanyName(post.getCompanyName());
        request.setRecruitType(post.getRecruitType());
        request.setDeadLine(post.getDeadLine());
        request.setReview(post.getReview());

        mockMvc.perform(post("/api/post")
                .header("api_key", "Bearer " + APITOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

}
