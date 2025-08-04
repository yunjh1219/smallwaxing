package com.example.smallwaxing.faq.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    RESERVATION("예약"),
    SERVICE("서비스"),
    AVAILABLE("시술가능여부"),
    AFTERCARE("왁싱후관리");

    private final String name;

    public static Category of(String koreanName) {
        if (koreanName == null) {
            return null;
        }
        if (koreanName.equals("예약")) {
            return RESERVATION;
        } else if (koreanName.equals("서비스")) {
            return SERVICE;
        } else if (koreanName.equals("시술가능여부")) {
            return AVAILABLE;
        } else if (koreanName.equals("왁싱후관리")) {
            return AFTERCARE;
        } else {
            return null;  // 혹은 예외 던지기
        }
    }
}