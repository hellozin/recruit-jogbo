package univ.study.recruitjogbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.post.PostService;
import univ.study.recruitjogbo.post.RecruitType;
import univ.study.recruitjogbo.post.RecruitTypeRepository;
import univ.study.recruitjogbo.post.RecruitTypes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

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

    public class Runner implements CommandLineRunner {

        @Autowired
        MemberService memberService;

        @Autowired
        PostService postService;

        @Autowired
        RecruitTypeRepository recruitTypeRepository;

        @Override
        public void run(String... args) {
            for (RecruitTypes recruitType : RecruitTypes.values()) {
                recruitTypeRepository.save(new RecruitType(recruitType));
            }

            Member member = memberService.join("admin", "admin", "paul5813@ynu.ac.kr");
            member.setEmailConfirmed(true);
            Member confirmed = memberService.save(member);

            postService.write(confirmed.getId(),"삼성", new HashSet<>(Arrays.asList(RecruitTypes.RESUME)), LocalDate.now(), "이러저러했다.");
            postService.write(confirmed.getId(),"삼성", new HashSet<>(Arrays.asList(RecruitTypes.INTERVIEW)), LocalDate.now(), "이러저러했다.");
            postService.write(confirmed.getId(),"라인", new HashSet<>(Arrays.asList(RecruitTypes.INTERVIEW)), LocalDate.now().minusDays(10), "저러이러했다.");
            postService.write(confirmed.getId(),"라인", new HashSet<>(Arrays.asList(RecruitTypes.CODING)), LocalDate.now().minusDays(2), "저러이러했다.");
        }
    }

}
