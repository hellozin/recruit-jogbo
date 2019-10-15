package univ.study.recruitjogbo.post.recruitType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitTypeRepository extends JpaRepository<RecruitType, Long> {
    List<RecruitType> findByRecruitTypeIn(List<RecruitTypes> recruitTypes);
}