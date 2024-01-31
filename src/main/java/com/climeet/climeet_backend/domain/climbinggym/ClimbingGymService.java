package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymRequestDto.UpdateClimbingGymInfoRequest;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.AcceptedClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymDetailResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.LayoutDetailResponse;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.s3.S3Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ClimbingGymService {

    private final ClimbingGymRepository climbingGymRepository;
    private final S3Service s3Service;

    public PageResponseDto<List<ClimbingGymSimpleResponse>> searchClimbingGym(String gymName,
        int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<ClimbingGym> climbingGymSlice = climbingGymRepository.findByNameContaining(gymName,
            pageable);

        List<ClimbingGymSimpleResponse> climbingGymList = climbingGymSlice.stream()
            .map(climbingGym -> ClimbingGymSimpleResponse.toDTO(climbingGym)).toList();

        return new PageResponseDto<>(pageable.getPageNumber(), climbingGymSlice.hasNext(),
            climbingGymList);

    }

    public PageResponseDto<List<AcceptedClimbingGymSimpleResponse>> searchAcceptedClimbingGym(
        String gymName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<ClimbingGym> climbingGymSlice = climbingGymRepository.findByNameContainingAndManagerIsNotNull(
            gymName, pageable);

        List<AcceptedClimbingGymSimpleResponse> climbingGymList = climbingGymSlice.stream()
            .map(climbingGym -> {
                Long managerId = null;
                Long follower = 0L;
                String profileImageUrl = null;
                // manager 유무 확인
                if (climbingGym.getManager() != null) {
                    managerId = climbingGym.getManager().getId();
                    // manager가 있으면 follower 조회
                    if (climbingGym.getManager().getFollowerCount() != null) {
                        follower = climbingGym.getManager().getFollowerCount();
                    }
                }
                // 프로필이 있으면 조회
                if (climbingGym.getProfileImageUrl() != null) {
                    profileImageUrl = climbingGym.getProfileImageUrl();
                }

                return AcceptedClimbingGymSimpleResponse.toDTO(climbingGym, managerId, follower,
                    profileImageUrl);
            }).toList();

        return new PageResponseDto<>(pageable.getPageNumber(), climbingGymSlice.hasNext(),
            climbingGymList);

    }

    public LayoutDetailResponse changeLayoutImage(MultipartFile layoutImage, Long gymId) {
        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        String layoutImageUrl = s3Service.uploadFile(layoutImage).getImgUrl();
        climbingGym.changeLayoutImageUrl(layoutImageUrl);

        climbingGymRepository.save(climbingGym);

        return LayoutDetailResponse.toDto(layoutImageUrl);
    }

    public ClimbingGymDetailResponse updateClimbingGymInfo(
        UpdateClimbingGymInfoRequest updateClimbingGymInfoRequest) {
        ClimbingGym climbingGym = climbingGymRepository.findById(
                updateClimbingGymInfoRequest.getGymId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        RestClient restClient = RestClient.create();

        String callUrl = "https://i9jkabugud.execute-api.ap-northeast-2.amazonaws.com/dev/gym?word="
            + climbingGym.getName();

        String gymInfoResult = restClient.get().uri(callUrl).retrieve().body(String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(gymInfoResult);
            // String name = jsonNode.get("name").asText();
            String tel = jsonNode.get("tel").asText();
            String address = jsonNode.get("address").asText();
            String businessHours = jsonNode.get("businessHours").toString();
            climbingGym.updateGymInfo(tel, address, businessHours);
            Map<String, List<String>> businessHoursMap = objectMapper.readValue(businessHours, new TypeReference<Map<String, List<String>>>() {});
            return ClimbingGymDetailResponse.toDto(climbingGymRepository.save(climbingGym), businessHoursMap);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._ERROR_JSON_PARSE);
        }
    }
}