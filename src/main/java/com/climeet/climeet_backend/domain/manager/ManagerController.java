package com.climeet.climeet_backend.domain.manager;

import com.climeet.climeet_backend.domain.manager.dto.ManagerRequestDto.CreateManagerRequest;
import com.climeet.climeet_backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Manager", description = "관리자 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/manager")
@CrossOrigin
public class ManagerController {
    private final ManagerService managerService;

    /**
     * [POST] 관리자 회원가입 API
     */
    @PostMapping("/signup")
    @Operation(summary = "관리자 회원가입", description = "관리자 회원가입 API")
    public ApiResponse<String> signUp(@RequestBody
        CreateManagerRequest createManagerRequest){
        managerService.signUp(createManagerRequest);
       return ApiResponse.onSuccess("관리자 회원가입이 성공적으로 완료되었습니다. ");
    }


    /**
     * [GET] 암장 관리자 등록 여부 확인
     */
    @Operation(summary = "암장 등록 중복 확인", description = "이미 관리자가 등록된 암장인지 확인하는 API \n\n **이미 관리자 등록 되어있음** : false \n\n **관리자 등록 안되어 있음** : true")
    @GetMapping("/isRegistered/{gymName}")
    public ApiResponse<Boolean> isRegistered(@PathVariable String gymName){
        boolean isRegistered = !managerService.checkManagerRegistration(gymName);
        return ApiResponse.onSuccess(isRegistered);
    }


    /**
     * [GET] 관리자 ID 중복확인
     */
    @GetMapping("/check-id/{loginId}")
    @Operation(summary = "관리자 ID 중복 확인", description = "**이미 존재하는 ID** : false \n\n **사용 가능한 ID** : true")
    public ApiResponse<Boolean> checkLoginId(@PathVariable String loginId){
        boolean isDuplicated = !managerService.checkLoginDuplication(loginId);
        return ApiResponse.onSuccess(isDuplicated);
    }



}
