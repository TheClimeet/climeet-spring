package com.climeet.climeet_backend.global.common;

import com.climeet.climeet_backend.domain.route.Route;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResponseDto<T> {

    private int page;
    private boolean hasNext;
    private T result;


}