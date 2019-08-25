package univ.study.recruitjogbo.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.validator.UnivEmail;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"password", "posts"})
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 25)
    private String username;

    @Size(min = 4, max = 25)
    @JsonIgnore
    private String password;

    @UnivEmail
    private String email;

    @NotNull
    private boolean emailConfirmed;

    @OneToMany(mappedBy = "author")
    @JsonManagedReference
    private Set<Post> posts;

    @Builder
    public Member(String username, String password, String email) {
        this.username = username;
        this.password = password;
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

    public Member setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
        return this;
    }
}
