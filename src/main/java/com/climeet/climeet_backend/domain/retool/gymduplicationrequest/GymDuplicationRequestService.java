package com.climeet.climeet_backend.domain.retool.gymduplicationrequest;

import com.climeet.climeet_backend.domain.retool.gymduplicationrequest.dto.GymDuplicationRequestResponse.GymDuplicationRequestSimpleInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GymDuplicationRequestService {

    private final GymDuplicationRequestRepository gymDuplicationRequestRepository;

    public List<GymDuplicationRequestSimpleInfo> getGymDuplicationRequests() {
        List<GymDuplicationRequest> gymDuplicationRequestList = gymDuplicationRequestRepository.findAll();

        return gymDuplicationRequestList.stream().map(gymDuplicationRequest ->
            GymDuplicationRequestSimpleInfo.toDTO(gymDuplicationRequest,
                gymDuplicationRequest.getManager())
        ).toList();
    }
}