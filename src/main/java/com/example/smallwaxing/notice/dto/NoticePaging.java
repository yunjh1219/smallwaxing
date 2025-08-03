package com.example.smallwaxing.notice.dto;

import lombok.Data;

@Data
public class NoticePaging {
    private int page;
    private int size;
    private String sort;
}