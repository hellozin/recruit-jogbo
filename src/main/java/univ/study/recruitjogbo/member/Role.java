package univ.study.recruitjogbo.member;

public enum Role {

    MEMBER("ROLE_MEMBER");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
