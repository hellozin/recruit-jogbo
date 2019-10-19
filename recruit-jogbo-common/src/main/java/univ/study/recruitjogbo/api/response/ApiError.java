package univ.study.recruitjogbo.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@ToString
public class ApiError {

    private final String errorMessage;

    private final HttpStatus status;

}
