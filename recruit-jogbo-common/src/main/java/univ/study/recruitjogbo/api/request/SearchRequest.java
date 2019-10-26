package univ.study.recruitjogbo.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import univ.study.recruitjogbo.review.ReviewSpecs;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
public class SearchRequest {

    private Map<ReviewSpecs.SearchKeys, Object> searchKeyMap;

    public Set<ReviewSpecs.SearchKeys> keySet() {
        return this.searchKeyMap.keySet();
    }

    public Object get(ReviewSpecs.SearchKeys keys) {
        return this.searchKeyMap.get(keys);
    }

    public boolean isEmpty() {
        return this.searchKeyMap.isEmpty();
    }

}
