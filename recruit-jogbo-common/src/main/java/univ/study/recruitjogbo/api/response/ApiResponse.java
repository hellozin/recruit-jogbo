package univ.study.recruitjogbo.api.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ApiResponse<T> {

    private boolean success;

    private T response;

    public static <T> ApiResponse<T> OK(T response) {
        return new ApiResponse<>(true, response);
    }

    public static ApiResponse<ApiError> ERROR(ApiError error) {
        return new ApiResponse<>(false, error);
    }

}
