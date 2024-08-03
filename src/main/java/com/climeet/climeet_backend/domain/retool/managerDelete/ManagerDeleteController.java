package com.climeet.climeet_backend.domain.retool.managerDelete;

import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.retool.managerDelete.dto.ManagerDeleteRequest.PatchManagerDeleteReq;
import com.climeet.climeet_backend.domain.retool.managerDelete.dto.ManagerDeleteResponse.GetManagerDeleteDetail;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class ManagerDeleteController {

    private final ManagerDeleteService managerDeleteService;

    @PatchMapping("/retool/managerDelete/{managerDeleteId}")
    @Operation(summary = "관리자 탈퇴")
    public ResponseEntity<String> approveManagerDelete(@PathVariable Long managerDeleteId, @RequestBody
        PatchManagerDeleteReq patchManagerDeleteReq){
        managerDeleteService.approveManagerDelete(managerDeleteId, patchManagerDeleteReq);
        return ResponseEntity.ok("승인 완료");
    }

    @GetMapping("/retool/managerDelete")
    @Operation(summary = "관리자 탈퇴 요청 조회")
    public ResponseEntity<List<GetManagerDeleteDetail>> getManagerDeleteRequest(){
        return ResponseEntity.ok(managerDeleteService.getManagerDeleteRequest());

    }





}
