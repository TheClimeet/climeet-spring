package com.climeet.climeet_backend.domain.routerecord.dto;

import com.climeet.climeet_backend.domain.routerecord.RouteRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RouteRecordResponseDto {
    @Getter
    @NoArgsConstructor
    public static class RouteRecordSimpleInfo {

        private Long routeRecordId;
        private Long climbingRecordId;
        private Long routeId;
        private Integer attemptCount;
        private Boolean isCompleted;

        public RouteRecordSimpleInfo(RouteRecord routeRecord) {
            this.routeRecordId = routeRecord.getId();
            this.climbingRecordId = routeRecord.getClimbingRecord().getId();
            this.routeId = routeRecord.getRoute().getId();
            this.attemptCount = routeRecord.getAttemptCount();
            this.isCompleted = routeRecord.getIsCompleted();

        }
    }
}
