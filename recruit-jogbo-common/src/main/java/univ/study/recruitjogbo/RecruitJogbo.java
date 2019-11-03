package univ.study.recruitjogbo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberRepository;
import univ.study.recruitjogbo.review.Review;
import univ.study.recruitjogbo.review.ReviewRepository;
import univ.study.recruitjogbo.review.recruitType.RecruitType;
import univ.study.recruitjogbo.review.recruitType.RecruitTypeEntity;
import univ.study.recruitjogbo.review.recruitType.RecruitTypeRepository;
import univ.study.recruitjogbo.tip.Tip;
import univ.study.recruitjogbo.tip.TipRepository;

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
    @Profile("local")
    @RequiredArgsConstructor
    class Runner implements CommandLineRunner {

        private final PasswordEncoder passwordEncoder;
        private final MemberRepository memberRepository;
        private final ReviewRepository reviewRepository;
        private final RecruitTypeRepository recruitTypeRepository;
        private final TipRepository tipRepository;

        @Override
        public void run(String... args) {
            for (RecruitType recruitType : RecruitType.values()) {
                recruitTypeRepository.save(new RecruitTypeEntity(recruitType));
            }

            Member member = new Member("admin", passwordEncoder.encode("admin"), "admin@ynu.ac.kr");
            memberRepository.save(member);

            LocalDate today = LocalDate.now();
            for (int i = 0; i < 10; i++) {
                List<RecruitTypeEntity> byRecruitTypeEntityIn = recruitTypeRepository.findByRecruitTypeIn(Arrays.asList(RecruitType.RESUME, RecruitType.APTITUDE));
                Review review = new Review(
                        member,
                        "company" + i,
                        "detail" + i,
                        byRecruitTypeEntityIn,
                        today.minusDays(i),
                        "review" + i);
                reviewRepository.save(review);
            }

            for (int i = 0; i < 10; i++) {
                Tip tip = new Tip(member, "title" + i, "content" + i);
                tipRepository.save(tip);
            }
        }
    }

}
