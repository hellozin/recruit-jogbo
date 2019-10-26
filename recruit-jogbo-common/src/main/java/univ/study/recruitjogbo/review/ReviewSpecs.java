package univ.study.recruitjogbo.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import univ.study.recruitjogbo.review.recruitType.RecruitType;
import univ.study.recruitjogbo.review.recruitType.RecruitTypeEntity;

import javax.persistence.criteria.Join;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public class ReviewSpecs {

    public enum SearchKeys {
        COMPANY_NAME("companyName"),
        RECRUIT_TYPE("recruitTypes"),
        AUTHOR_NAME("authorName");

        private String value;

        SearchKeys(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static Specification<Review> searchWith(Map<SearchKeys, Object> request) {
        if (request.isEmpty()) {
            log.warn("[request] is empty.");
            return Specification.where(null);
        }

        Specification<Review> spec = null;
        for (SearchKeys key : request.keySet()) {
            switch (key) {
                case COMPANY_NAME:
                    final String companyName = String.valueOf(request.get(key));
                    spec = appendSpec(spec, withCompanyName(companyName));
                    break;
                case RECRUIT_TYPE:
                    RecruitType recruitType = RecruitType.valueOf(String.valueOf(request.get(key)));
                    spec = appendSpec(spec, withRecruitType(recruitType));
                    break;
                case AUTHOR_NAME:
                    String authorName = String.valueOf(request.get(key));
                    spec = appendSpec(spec, withAuthorName(authorName));
                    break;
            }
        }
        return spec;
    }

    private static Specification<Review> appendSpec(Specification<Review> base, Specification<Review> append) {
        return base == null ? append : base.and(append);
    }

    public static Specification<Review> withCompanyName(String companyName) {
        if (isBlank(companyName)) {
            log.warn("[companyName] is empty.");
            return Specification.where(null);
        }
        return (Specification<Review>) (root, query, builder) -> builder.like(root.get("companyName"), companyName);
    }

    public static Specification<Review> withRecruitType(RecruitType recruitType) {
        if (recruitType == null) {
            log.warn("[recruitTypes] is empty.");
            return Specification.where(null);
        }
        return (Specification<Review>) (root, query, builder) -> {
            Join<Review, RecruitTypeEntity> joined = root.join("recruitTypes");
            return builder.equal(joined.get("recruitType"), recruitType);
        };
    }

    public static Specification<Review> withAuthorName(String authorName) {
        if (isBlank(authorName)) {
            log.warn("[authorName] is empty.");
            return Specification.where(null);
        }
        return (Specification<Review>) (root, query, builder) -> builder.equal(root.get("author").get("username"), authorName);
    }

}
