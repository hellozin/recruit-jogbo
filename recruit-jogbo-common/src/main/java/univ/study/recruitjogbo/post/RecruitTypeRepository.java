package univ.study.recruitjogbo.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RecruitTypeRepository extends JpaRepository<RecruitType, Long> {
    Set<RecruitType> findByRecruitTypeIn(Set<RecruitTypes> recruitTypes);
}
