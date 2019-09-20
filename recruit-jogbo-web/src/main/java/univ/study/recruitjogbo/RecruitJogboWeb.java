package univ.study.recruitjogbo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.post.RecruitType;
import univ.study.recruitjogbo.post.RecruitTypeRepository;
import univ.study.recruitjogbo.post.RecruitTypes;
import univ.study.recruitjogbo.request.JoinRequest;

@SpringBootApplication
public class RecruitJogboWeb {

    private static final String PROPERTIES =
            "spring.config.location="
                    +"classpath:/application.yml"
                    +",file:./secret-datasource.yml"
                    +",file:./secret-rabbitmq.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(RecruitJogboWeb.class)
                .properties(PROPERTIES)
                .run(args);
    }

    @Component
    @RequiredArgsConstructor
    public class Runner implements CommandLineRunner {

        private final RecruitTypeRepository recruitTypeRepository;

        private final MemberService memberService;

        @Override
        public void run(String... args) {
            for (RecruitTypes recruitType : RecruitTypes.values()) {
                recruitTypeRepository.save(new RecruitType(recruitType));
            }

            JoinRequest joinRequest = new JoinRequest();
            joinRequest.setUsername("admin");
            joinRequest.setPassword("admin");
            joinRequest.setEmail("paul5813@ynu.ac.kr");
            Member member = memberService.join(joinRequest);
            member.setEmailConfirmed(true);
            memberService.save(member);
        }
    }

}
