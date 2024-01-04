package com.climeet.climeet_backend.domain.routerecord;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class RouteRecordController {
    private final RouteRecordService routeRecordService;

}