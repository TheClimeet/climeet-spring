package com.climeet.climeet_backend.domain.manager;

import com.climeet.climeet_backend.domain.manager.dto.ManagerRequestDto.CreateAccessTokenRequest;
import com.climeet.climeet_backend.domain.manager.dto.ManagerRequestDto.CreateManagerRequest;
import com.climeet.climeet_backend.domain.manager.dto.ManagerResponseDto.ManagerSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("api/manager")
@CrossOrigin
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인", description = "관리자 로그인")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._WRONG_LOGINID_PASSWORD})
    public ResponseEntity<ManagerSimpleInfo> login(@RequestBody
        CreateAccessTokenRequest createAccessTokenRequest){
        ManagerSimpleInfo managerSimpleInfo = managerService.login(createAccessTokenRequest);
        return ResponseEntity.ok(managerSimpleInfo);
    }

    @PostMapping("/signup")
    @Operation(summary = "관리자 회원가입", description = "**Enum 설명**\n\n**ServiceBitmask** :  샤워\\_시설,  샤워\\_용품,  수건\\_제공,  간이\\_세면대,  초크\\_대여,  암벽화\\_대여,  삼각대\\_대여,  운동복\\_대여")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._DUPLICATE_LOGINID})
    public ResponseEntity<String> signUp(@RequestBody
        CreateManagerRequest createManagerRequest){
        managerService.signUp(createManagerRequest);
       return ResponseEntity.ok("회원가입 완료. 승인 대기 중.");
    }

    @Operation(summary = "암장 등록 중복 확인", description = "이미 관리자가 등록된 암장인지 확인하는 API \n\n **이미 관리자 등록 되어있음** : false \n\n **관리자 등록 안되어 있음** : true")
    @GetMapping("/isRegistered/{gymId}")
    public ResponseEntity<Boolean> isRegistered(@PathVariable Long gymId){
        boolean isRegistered = !managerService.checkManagerRegistration(gymId);
        return ResponseEntity.ok(isRegistered);
    }


    @GetMapping("/check-id/{loginId}")
    @Operation(summary = "관리자 ID 중복 확인", description = "**이미 존재하는 ID** : false \n\n **사용 가능한 ID** : true")
    public ResponseEntity<Boolean> checkLoginId(@PathVariable String loginId){
        boolean isDuplicated = !managerService.checkLoginDuplication(loginId);
        return ResponseEntity.ok(isDuplicated);

    }

    @GetMapping("/gym-id")
    @Operation(summary = "관리자 관리 암장 확인. - 205 [무빗]", description = "관리자의 소속 암장 id를 확인합니다.")
    public ResponseEntity<Long> getClimbingGymIdOfManager(@CurrentUser User user){
        return ResponseEntity.ok(managerService.getClimbingGymIdOfManager(user));
    }



}
