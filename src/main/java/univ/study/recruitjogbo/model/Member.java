package univ.study.recruitjogbo.model;

public class Member {

    private final Long seq;

    private final String userId;

    private final String password;

    private final String name;

    public Member(String userId, String password, String name) {
        this(null, userId, password, name);
    }

    public Member(Long seq, String userId, String password, String name) {
        this.seq = seq;
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

}
