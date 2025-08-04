package com.example.smallwaxing.faq.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FaqSearchCondition {

    private String category;

    @Builder
    public FaqSearchCondition(String category) {
        this.category = category;
    }
}