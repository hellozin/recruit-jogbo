package univ.study.recruitjogbo;

public interface ConfirmEmailTemplate {

    String subject = "[Recruit Jogbo] 사용자 인증 메일입니다.";

    String baseUrl = "http://localhost:8080/api/confirm/email?token=";

}
