package com.climeet.climeet_backend.domain.routeversion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RouteVersionConverter {

    // "[1,2,3,4,5]" -> [1, 2, 3, 4, 5]
    public static List<Long> convertStringToList(String stringToConvert) {

        String[] longList = stringToConvert.substring(1, stringToConvert.length() - 1).split(",");
        return Arrays.stream(longList)
            .map(Long::parseLong)
            .toList();
    }

    // [1, 2, 3, 4, 5] -> "[1,2,3,4,5]"
    public static String convertListToString(List<Long> listToConvert) {
        String result = listToConvert.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","));
        return "[" + result + "]";
    }

}
