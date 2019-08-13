package univ.study.recruitjogbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.member.RecruitType;
import univ.study.recruitjogbo.post.PostService;

import java.time.LocalDate;

@SpringBootApplication
public class RecruitJogboWeb {

    private static final String PROPERTIES =
            "spring.config.location="
                    +"classpath:/application.yml"
                    +",classpath:/secret.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(RecruitJogboWeb.class)
                .properties(PROPERTIES)
                .run(args);
    }

    @Component
    public class Runner implements CommandLineRunner {

        @Autowired
        MemberService memberService;

        @Autowired
        PostService postService;

        @Override
        public void run(String... args) {
            Member member = memberService.join("paul", "1234", "hellozin", "hello@gmail.com");
            postService.write(member.getId(),"삼성", RecruitType.RESUME, LocalDate.now(), "이러저러했다.");
            postService.write(member.getId(),"라인", RecruitType.INTERVIEW, LocalDate.now(), "저러이러했다.");
        }
    }

}
