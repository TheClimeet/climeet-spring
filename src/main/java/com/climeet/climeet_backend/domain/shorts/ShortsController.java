package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.shorts.dto.ShortsRequestDto.PostShortsReq;
import com.climeet.climeet_backend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class ShortsController {

    private final ShortsService shortsService;

    @PostMapping("/shorts")
    public ApiResponse<String> uploadShorts(@RequestPart(value = "video") MultipartFile video,
        @RequestPart MultipartFile thumbnailImage,
        @RequestPart PostShortsReq postShortsReq) {
        shortsService.uploadShorts(video, thumbnailImage, postShortsReq);
        return ApiResponse.onSuccess("업로드 성공");
    }
}