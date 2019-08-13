package univ.study.recruitjogbo.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import univ.study.recruitjogbo.member.RecruitType;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCompanyName(String companyName, Pageable pageable);

    Page<Post> findByRecruitType(RecruitType recruitType, Pageable pageable);

    Page<Post> findByAuthor_Id(Long authorId, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

}
