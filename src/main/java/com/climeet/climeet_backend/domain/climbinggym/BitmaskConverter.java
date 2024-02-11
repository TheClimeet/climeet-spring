package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.enums.ServiceBitmask;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BitmaskConverter {

    // Service List → Bitmask
    public static int convertServiceListToBitmask(List<ServiceBitmask> serviceList) {
        int bitmask = 0;
        for (ServiceBitmask serviceBitMask : serviceList) {
            bitmask |= serviceBitMask.getValue();
        }
        return bitmask;
    }

    // Bitmask → Service List
    public static List<ServiceBitmask> convertBitmaskToServiceList(int bitmask) {
        List<ServiceBitmask> serviceList = new ArrayList<>();
        for (ServiceBitmask service : ServiceBitmask.values()) {
            if ((bitmask & service.getValue()) != 0) {
                serviceList.add(service);
            }
        }
        return serviceList;
    }
}
