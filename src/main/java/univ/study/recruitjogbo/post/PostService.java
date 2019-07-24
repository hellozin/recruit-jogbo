package univ.study.recruitjogbo.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import univ.study.recruitjogbo.member.RecruitType;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post write(String companyName, RecruitType recruitType, LocalDate deadLine, String review) {
        return save(new Post.PostBuilder()
                .companyName(companyName)
                .recruitType(recruitType)
                .deadLine(deadLine)
                .review(review)
                .build()
        );
    }

    public List<Post> findByCompanyName(String companyName) {
        return postRepository.findByCompanyNameOrderByCreatedDateDesc(companyName);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

}
