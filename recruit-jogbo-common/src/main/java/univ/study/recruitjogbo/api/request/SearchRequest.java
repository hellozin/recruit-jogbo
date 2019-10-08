package univ.study.recruitjogbo.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import univ.study.recruitjogbo.post.PostSpecs;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
public class SearchRequest {

    private Map<PostSpecs.SearchKeys, Object> searchKeyMap;

    public Set<PostSpecs.SearchKeys> keySet() {
        return this.searchKeyMap.keySet();
    }

    public Object get(PostSpecs.SearchKeys keys) {
        return this.searchKeyMap.get(keys);
    }

    public boolean isEmpty() {
        return this.searchKeyMap.isEmpty();
    }

}
