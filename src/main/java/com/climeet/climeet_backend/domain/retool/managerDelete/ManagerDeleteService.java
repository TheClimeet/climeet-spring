package com.climeet.climeet_backend.domain.retool.managerDelete;

import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.retool.managerDelete.dto.ManagerDeleteRequest.PatchManagerDeleteReq;
import com.climeet.climeet_backend.domain.retool.managerDelete.dto.ManagerDeleteResponse.GetManagerDeleteDetail;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerDeleteService {

    private final ManagerDeleteRepository managerDeleteRepository;

    @Transactional
    public void approveManagerDelete(Long managerDeleteId, PatchManagerDeleteReq patchManagerDeleteReq){
        ManagerDelete managerDelete = managerDeleteRepository.findById(managerDeleteId)
            .orElseThrow(()-> new GeneralException(ErrorStatus._EMPTY_MANAGER_DELETE_REQUEST));
        Manager manager = managerDelete.getManager();
        if(patchManagerDeleteReq.getIsApproved()){
            manager.updateStatus();
            manager.setLastLogin(LocalDateTime.now());
        }
        managerDelete.updateApprovalStatus(patchManagerDeleteReq.getIsApproved());

    }

    public List<GetManagerDeleteDetail> getManagerDeleteRequest(){
        List<ManagerDelete> managerDeletes = managerDeleteRepository.findAll();

        return managerDeletes.stream().map(
            GetManagerDeleteDetail::toDTO
        ).toList();

    }


}
