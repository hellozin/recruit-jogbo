package univ.study.recruitjogbo.message;

public interface RabbitMQ {

    String EXCHANGE = "recruit.jogbo";
    String CONFIRM_EMAIL_REQUEST = "confirm.email";
    String REVIEW_CREATE = "review.create";
    String REVIEW_UPDATE = "review.update";
    String TIP_CREATE = "tip.create";
    String TIP_UPDATE = "tip.update";

}
