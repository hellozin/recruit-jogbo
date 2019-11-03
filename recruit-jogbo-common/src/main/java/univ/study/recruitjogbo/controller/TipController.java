package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.api.request.TipPublishRequest;
import univ.study.recruitjogbo.api.response.ApiResponse;
import univ.study.recruitjogbo.security.JwtAuthentication;
import univ.study.recruitjogbo.tip.Tip;
import univ.study.recruitjogbo.tip.TipService;

import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TipController {

    private final TipService tipService;

    private final PagedResourcesAssembler<Tip> assembler;

    @PostMapping("/tip")
    public ApiResponse createTip(@AuthenticationPrincipal JwtAuthentication jwtAuthentication,
                               @RequestBody @Valid TipPublishRequest tipPublishRequest) {
        return ApiResponse.OK(tipService.publish(jwtAuthentication.id, tipPublishRequest));
    }

    @GetMapping("/tip/list")
    public ApiResponse getTipList(@PageableDefault(sort = {"createdAt"}, direction = DESC) Pageable pageable) {
        return ApiResponse.OK(assembler.toResource(tipService.findAll(pageable)));
    }

    @GetMapping("/tip/{id}")
    public ApiResponse getTip(@PathVariable(value = "id") Tip tip) {
        return ApiResponse.OK(tip);
    }

    @PutMapping("/tip/{id}")
    public ApiResponse editTip(@AuthenticationPrincipal JwtAuthentication jwtAuthentication,
                               @PathVariable(value = "id") Long tipId,
                               @RequestBody @Valid TipPublishRequest tipPublishRequest) {
        return ApiResponse.OK(tipService.edit(jwtAuthentication.id, tipId, tipPublishRequest));
    }

    @DeleteMapping("/tip/{id}")
    public ApiResponse deleteTip(@AuthenticationPrincipal JwtAuthentication jwtAuthentication,
                                 @PathVariable(value = "id") Long tipId) {
        tipService.delete(jwtAuthentication.id, tipId);
        return ApiResponse.OK(tipId);
    }

}
