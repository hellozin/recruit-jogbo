package univ.study.recruitjogbo.post;

import org.springframework.data.jpa.domain.Specification;
import univ.study.recruitjogbo.member.RecruitType;

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

    public static Specification<Post> searchWith(SearchRequest request) {
        if (request.isEmpty()) {
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
        return (Specification<Post>) (root, query, builder) -> builder.like(root.get("companyName"), companyName);
    }

    public static Specification<Post> withRecruitType(RecruitType recruitType) {
        return (Specification<Post>) (root, query, builder) -> builder.equal(root.get("recruitType"), recruitType);
    }

    public static Specification<Post> withAuthorId(Long authorId) {
        return (Specification<Post>) (root, query, builder) -> builder.equal(root.get("author").get("id"), authorId);
    }

}
