package univ.study.recruitjogbo.post;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import univ.study.recruitjogbo.member.RecruitType;

import java.util.Map;

@Slf4j
public class PostSpecs {

    public enum SearchKeys {
        COMPANY_NAME("companyName"),
        RECRUIT_TYPE("recruitType"),
        AUTHOR_ID("authorId");

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
                    RecruitType recruitType = RecruitType.valueOf(String.valueOf(request.get(key)));
                    spec = appendSpec(spec, withRecruitType(recruitType));
                    break;
                case AUTHOR_ID:
                    Long authorId = Long.valueOf(String.valueOf(request.get(key)));
                    spec = appendSpec(spec, withAuthorId(authorId));
                    break;
            }
        }
        return spec;
    }

    private static Specification<Post> appendSpec(Specification<Post> base, Specification<Post> append) {
        return base == null ? append : base.and(append);
    }

    public static Specification<Post> withCompanyName(String companyName) {
        if (StringUtils.isBlank(companyName)) {
            log.warn("[companyName] is empty.");
            return Specification.where(null);
        }
        return (Specification<Post>) (root, query, builder) -> builder.like(root.get("companyName"), companyName);
    }

    public static Specification<Post> withRecruitType(RecruitType recruitType) {
        if (recruitType == null) {
            log.warn("[recruitType] is empty.");
            return Specification.where(null);
        }
        return (Specification<Post>) (root, query, builder) -> builder.equal(root.get("recruitType"), recruitType);
    }

    public static Specification<Post> withAuthorId(Long authorId) {
        if (authorId == null) {
            log.warn("[authorId] is empty.");
            return Specification.where(null);
        }
        return (Specification<Post>) (root, query, builder) -> builder.equal(root.get("author").get("id"), authorId);
    }

}
