package univ.study.recruitjogbo.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ApiResponse<T> {

    private final boolean success;

    private final T response;

    public static <T> ApiResponse<T> OK(T response) {
        return new ApiResponse<>(true, response);
    }

    public static ApiResponse<ApiError> ERROR(ApiError error) {
        return new ApiResponse<>(false, error);
    }

}
