package univ.study.recruitjogbo.request;

import univ.study.recruitjogbo.post.PostSpecs;

import java.util.Map;
import java.util.Set;

public class SearchRequest {

    private Map<PostSpecs.SearchKeys, Object> searchKeyMap;

    public SearchRequest(Map<PostSpecs.SearchKeys, Object> searchKeyMap) {
        this.searchKeyMap = searchKeyMap;
    }

    public Set<PostSpecs.SearchKeys> keySet() {
        return this.searchKeyMap.keySet();
    }

    public Object get(PostSpecs.SearchKeys keys) {
        return this.searchKeyMap.get(keys);
    }

    public Map<PostSpecs.SearchKeys, Object> getMap() {
        return this.searchKeyMap;
    }

    public boolean isEmpty() {
        return this.searchKeyMap.isEmpty();
    }

}
