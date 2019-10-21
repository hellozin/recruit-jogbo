package univ.study.recruitjogbo.api.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConfirmMailSendingRequest {

    private String confirmUrl;

}
