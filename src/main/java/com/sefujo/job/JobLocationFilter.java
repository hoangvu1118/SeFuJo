package com.sefujo.job;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@AllArgsConstructor
@Component
public class JobLocationFilter {
    public boolean isTargetLocation(String location) {
        if (location == null) {
            return false;
        }

        return location.contains("vietnam")
                || location.contains("viet nam")
                || location.contains("ho chi minh")
                || location.contains("hanoi")
                || location.contains("ha noi")
                || location.contains("da nang")
                || location.contains("singapore");
    }
}
