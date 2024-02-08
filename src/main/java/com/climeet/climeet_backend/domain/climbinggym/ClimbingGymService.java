package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymRequestDto.UpdateClimbingGymInfoRequest;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.AcceptedClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymDetailResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymInfoResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymTabInfoResponse;
import com.climeet.climeet_backend.domain.climbinggym.enums.ServiceBitmask;
import com.climeet.climeet_backend.domain.climbinggymimage.ClimbingGymBackgroundImage;
import com.climeet.climeet_backend.domain.climbinggymimage.ClimbingGymBackgroundImageRepository;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class ClimbingGymService {

    private final ClimbingGymRepository climbingGymRepository;
    private final ManagerRepository managerRepository;
    private final ClimbingGymBackgroundImageRepository climbingGymBackgroundImageRepository;
    private final BitmaskConverter bitmaskConverter;

    @Value("${cloud.aws.lambda.crawling-uri}")
    private String crawlingUri;

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

    public ClimbingGymDetailResponse getClimbingGymInfo(Long gymId) {
        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        Manager manager = managerRepository.findByClimbingGym(climbingGym)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER));

        ClimbingGymBackgroundImage backgroundImage = climbingGymBackgroundImageRepository.findByClimbingGym(
                climbingGym)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_BACKGROUND_IMAGE));

        return ClimbingGymDetailResponse.toDto(climbingGym, manager, backgroundImage.getImgUrl());
    }

    public ClimbingGymTabInfoResponse getClimbingGymTabInfo(Long gymId) {
        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        List<String> serviceList = bitmaskConverter.convertBitmaskToServiceList(
            climbingGym.getServiceBitMask()).stream().map(ServiceBitmask::name).toList();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, List<String>> businessHoursMap = objectMapper.readValue(
                climbingGym.getBusinessHours(),
                new TypeReference<Map<String, List<String>>>() {
                });
            return ClimbingGymTabInfoResponse.toDto(climbingGymRepository.save(climbingGym),
                businessHoursMap, serviceList);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._ERROR_JSON_PARSE);
        }
    }


    public ClimbingGymInfoResponse updateClimbingGymInfo(

        UpdateClimbingGymInfoRequest updateClimbingGymInfoRequest) {
        ClimbingGym climbingGym = climbingGymRepository.findById(
                updateClimbingGymInfoRequest.getGymId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        RestClient restClient = RestClient.create();

        String callUrl = crawlingUri + "?word=" + climbingGym.getName();

        String gymInfoResult = restClient.get().uri(callUrl).retrieve().body(String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(gymInfoResult);
            // String name = jsonNode.get("name").asText();
            String tel = jsonNode.get("tel").asText();
            String address = jsonNode.get("address").asText();
            String businessHours = jsonNode.get("businessHours").toString();
            climbingGym.updateGymInfo(tel, address, businessHours);
            Map<String, List<String>> businessHoursMap = objectMapper.readValue(businessHours,
                new TypeReference<Map<String, List<String>>>() {
                });
            return ClimbingGymInfoResponse.toDto(climbingGymRepository.save(climbingGym),
                businessHoursMap);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._ERROR_JSON_PARSE);
        }
    }
}