package univ.study.recruitjogbo.member;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import univ.study.recruitjogbo.post.Post;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"password", "posts"})
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 25)
    private String memberId;

    @NotBlank
    @JsonIgnore
    private String password;

    @NotBlank
    @Size(min = 4, max = 25)
    private String name;

    @Email
    private String email;

    @OneToMany(mappedBy = "author")
    @JsonManagedReference
    private Set<Post> posts;

    @Builder
    public Member(String memberId, String password, String name, String email) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public boolean checkPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    protected Set<Post> getPostsInternal() {
        if (this.posts == null) {
            this.posts = new HashSet<>();
        }
        return this.posts;
    }

    public List<Post> getPosts() {
        ArrayList<Post> sortedPosts = new ArrayList<>(getPostsInternal());
        PropertyComparator.sort(sortedPosts, new MutableSortDefinition("createdDate", true, true));
        return Collections.unmodifiableList(sortedPosts);
    }

    public void addPost(Post post) {
        getPostsInternal().add(post);
        post.setAuthor(this);
    }

}
