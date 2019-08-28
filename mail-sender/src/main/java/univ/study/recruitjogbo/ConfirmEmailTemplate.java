package univ.study.recruitjogbo;

public interface ConfirmEmailTemplate {

    String subject = "[Recruit Jogbo] 사용자 인증 메일입니다.";

    String text = "이메일을 인증하기 위해 다음 링크를 클릭해 주세요.\n 링크: " +
            "http://localhost:8080/api/confirm/email?token=";

}
