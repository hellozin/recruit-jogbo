package univ.study.recruitjogbo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberRepository;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.post.PostRepository;
import univ.study.recruitjogbo.post.recruitType.RecruitType;
import univ.study.recruitjogbo.post.recruitType.RecruitTypeRepository;
import univ.study.recruitjogbo.post.recruitType.RecruitTypes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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

        private final MemberRepository memberRepository;

        private final PostRepository postRepository;

        private final RecruitTypeRepository recruitTypeRepository;

        private final PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) {
            for (RecruitTypes recruitType : RecruitTypes.values()) {
                recruitTypeRepository.save(new RecruitType(recruitType));
            }

            Member member = new Member("username", passwordEncoder.encode("password"), "user@ynu.ac.kr");
            memberRepository.save(member);

            LocalDate today = LocalDate.now();
            for (int i = 0; i < 10; i++) {
                List<RecruitType> byRecruitTypeIn = recruitTypeRepository.findByRecruitTypeIn(Arrays.asList(RecruitTypes.RESUME, RecruitTypes.APTITUDE));
                Post post = new Post(
                        member,
                        "company" + i,
                        "detail" + i,
                        byRecruitTypeIn,
                        today.minusDays(i),
                        "review" + i);
                postRepository.save(post);
            }
        }
    }

}
