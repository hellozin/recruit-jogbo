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
import univ.study.recruitjogbo.api.request.ReviewPublishRequest;
import univ.study.recruitjogbo.api.response.ApiResponse;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberRepository;
import univ.study.recruitjogbo.review.recruitType.RecruitType;

import java.time.LocalDate;
import java.util.LinkedHashMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Autowired
    MemberRepository memberRepository;

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
        JoinRequest joinRequest = new JoinRequest() {
            @Override public String getUsername() { return member.getUsername(); }
            @Override public String getPassword() { return member.getPassword(); }
            @Override public String getEmail() { return member.getEmail(); }
            @Override public String getConfirmUrl() { return ""; }
        };

        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(joinRequest))
                .with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.email").value(member.getEmail()));
    }

    @Test
    @Order(2)
    void 멤버_인증토큰을_발행한다() throws Exception {
        Member find = memberRepository.findByUsername(this.member.getUsername())
                .orElseThrow(() -> new NotFoundException(Member.class, this.member.getUsername()));
        find.setEmailConfirmed(true);
        memberRepository.save(find);

        AuthenticationRequest request = new AuthenticationRequest(this.member.getUsername(), this.member.getPassword());

        MvcResult mvcResult = mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
                .with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ApiResponse apiResponse = objectMapper.readValue(contentAsString, ApiResponse.class);
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) apiResponse.getResponse();
        APITOKEN = String.valueOf(map.get("apiToken"));
    }

    @Test
    @Order(3)
    void 리뷰_목록을_가져온다() throws Exception {
        mockMvc.perform(get("/api/review/list")
                .header("api_key", "Bearer " + APITOKEN)
                .with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void 새_리뷰를_작성한다() throws Exception {
        ReviewPublishRequest request = new ReviewPublishRequest() {
            @Override public String getCompanyName() { return "Company Name"; }
            @Override public String getCompanyDetail() { return "Company Detail"; }
            @Override public RecruitType[] getRecruitTypes() { return new RecruitType[]{RecruitType.RESUME}; }
            @Override public LocalDate getDeadLine() { return LocalDate.of(2019,1,1); }
            @Override public String getReview() { return "New review"; }
        };

        mockMvc.perform(post("/api/review")
                .header("api_key", "Bearer " + APITOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

}
