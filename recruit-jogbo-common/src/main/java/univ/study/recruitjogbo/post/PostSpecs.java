package univ.study.recruitjogbo.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public class PostSpecs {

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

    public static Specification<Post> searchWith(Map<SearchKeys, Object> request) {
        if (request.isEmpty()) {
            log.warn("[request] is empty.");
            return Specification.where(null);
        }

        Specification<Post> spec = null;
        for (SearchKeys key : request.keySet()) {
            switch (key) {
                case COMPANY_NAME:
                    final String companyName = String.valueOf(request.get(key));
                    spec = appendSpec(spec, withCompanyName(companyName));
                    break;
                case RECRUIT_TYPE:
                    RecruitTypes recruitTypes = RecruitTypes.valueOf(String.valueOf(request.get(key)));
                    spec = appendSpec(spec, withRecruitType(recruitTypes));
                    break;
                case AUTHOR_NAME:
                    String authorName = String.valueOf(request.get(key));
                    spec = appendSpec(spec, withAuthorName(authorName));
                    break;
            }
        }
        return spec;
    }

    private static Specification<Post> appendSpec(Specification<Post> base, Specification<Post> append) {
        return base == null ? append : base.and(append);
    }

    public static Specification<Post> withCompanyName(String companyName) {
        if (isBlank(companyName)) {
            log.warn("[companyName] is empty.");
            return Specification.where(null);
        }
        return (Specification<Post>) (root, query, builder) -> builder.like(root.get("companyName"), companyName);
    }

    public static Specification<Post> withRecruitType(RecruitTypes recruitType) {
        if (recruitType == null) {
            log.warn("[recruitTypes] is empty.");
            return Specification.where(null);
        }
        return (Specification<Post>) (root, query, builder) -> {
            Join<Post, RecruitTypes> joined = root.join("recruitTypes");
            return builder.equal(joined.get("recruitType"), recruitType);
        };
    }

    public static Specification<Post> withAuthorName(String authorName) {
        if (isBlank(authorName)) {
            log.warn("[authorName] is empty.");
            return Specification.where(null);
        }
        return (Specification<Post>) (root, query, builder) -> builder.equal(root.get("author").get("username"), authorName);
    }

}
