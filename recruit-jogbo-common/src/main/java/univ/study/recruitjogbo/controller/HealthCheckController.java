package univ.study.recruitjogbo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import univ.study.recruitjogbo.api.response.ApiResponse;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @GetMapping("/hcheck")
    public ApiResponse healthCheck() {
        return ApiResponse.OK(System.currentTimeMillis());
    }

}
