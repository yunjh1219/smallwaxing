package com.example.smallwaxing.faq.dto;

import lombok.Data;

@Data
public class FaqPaging {
    private int page;
    private int size;
    private String sort;
}