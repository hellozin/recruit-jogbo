package univ.study.recruitjogbo.post;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.study.recruitjogbo.member.RecruitType;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCompanyNameOrderByCreatedDateDesc(String companyName);

    List<Post> findByRecruitTypeOrderByCreatedDateDesc(RecruitType recruitType);

    List<Post> findByAuthor_IdOrderByCreatedDateDesc(Long authorId);

}
