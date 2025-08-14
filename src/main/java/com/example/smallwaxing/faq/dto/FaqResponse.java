package com.example.smallwaxing.faq.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FaqResponse {

    private Long id;
    private String content; //내용

    @Builder
    public FaqResponse(Long id, String content) {
        this.id = id;
        this.content = content;
    }

}


