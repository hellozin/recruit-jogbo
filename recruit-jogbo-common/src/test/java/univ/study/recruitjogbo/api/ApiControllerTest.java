package univ.study.recruitjogbo.api;

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
import univ.study.recruitjogbo.api.request.AuthenticationRequest;
import univ.study.recruitjogbo.api.request.JoinRequest;
import univ.study.recruitjogbo.api.request.PostingRequest;
import univ.study.recruitjogbo.api.response.AuthenticationResult;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.post.RecruitTypes;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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

    private String APITOKEN;

    @BeforeAll
    void setUp() {
        member = new Member("hellozin", "hello1234", "hello@yu.ac.kr");
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
        request.setCompanyName("Company Name");
        request.setRecruitTypes(Arrays.asList(RecruitTypes.RESUME));
        request.setDeadLine(LocalDate.of(2019,1,1));
        request.setReview("New review");

        mockMvc.perform(post("/api/post")
                .header("api_key", "Bearer " + APITOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

}
