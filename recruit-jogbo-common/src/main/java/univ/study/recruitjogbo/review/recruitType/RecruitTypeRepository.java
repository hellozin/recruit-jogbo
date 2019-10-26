package univ.study.recruitjogbo.review.recruitType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitTypeRepository extends JpaRepository<RecruitTypeEntity, Long> {
    List<RecruitTypeEntity> findByRecruitTypeIn(List<RecruitType> recruitTypes);
}
