package univ.study.recruitjogbo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;
import univ.study.recruitjogbo.api.request.JoinRequest;
import univ.study.recruitjogbo.api.request.PostingRequest;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.post.PostService;
import univ.study.recruitjogbo.post.recruitType.RecruitType;
import univ.study.recruitjogbo.post.recruitType.RecruitTypeRepository;
import univ.study.recruitjogbo.post.recruitType.RecruitTypes;

import java.time.LocalDate;

@SpringBootApplication
public class RecruitJogbo {

    private static final String PROPERTIES =
            "spring.config.location="
                    +"classpath:/application.yml"
                    +",file:./secret-datasource.yml"
                    +",file:./secret-rabbitmq.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(RecruitJogbo.class)
                .properties(PROPERTIES)
                .run(args);
    }

    @Component
    @RequiredArgsConstructor
    class Runner implements CommandLineRunner {

        private final MemberService memberService;

        private final PostService postService;

        private final RecruitTypeRepository recruitTypeRepository;

        @Override
        public void run(String... args) {
            for (RecruitTypes recruitType : RecruitTypes.values()) {
                recruitTypeRepository.save(new RecruitType(recruitType));
            }

            JoinRequest request = new JoinRequest();
            request.setUsername("username");
            request.setPassword("password");
            request.setEmail("user@ynu.ac.kr");
            Member join = memberService.join(request);

            LocalDate today = LocalDate.now();
            for (int i = 0; i < 10; i++) {
                PostingRequest postingRequest = new PostingRequest();
                postingRequest.setCompanyName("company" + i);
                postingRequest.setRecruitTypes(new RecruitTypes[]{RecruitTypes.RESUME, RecruitTypes.APTITUDE});
                postingRequest.setDeadLine(today.minusDays(i));
                postingRequest.setReview("review" + i);
                postService.write(join.getId(), postingRequest);
            }
        }
    }

}
