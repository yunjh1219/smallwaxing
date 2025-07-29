package com.example.smallwaxing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    //기본 페이지
    @GetMapping("/")
    public String main(){
        return "pages/main";
    }

    @GetMapping("/admin")
    public String admin(){
        return "pages/admin";
    }

    //스몰왁싱 카테고리
    @GetMapping("/directions") //오시는 길
    public String directions(){
        return "pages/smallwaxing/directions";
    }

    @GetMapping("/smallwaxing") //스몰왁싱 특별함
    public String smallwaxing(){
        return "pages/smallwaxing/smallwaxing";
    }

    //서비스 카테고리
    @GetMapping("/brazilianwaxing") //바디왁싱
    public String brazilianwaxing(){
        return "pages/service/brazilianwaxing";
    }
    @GetMapping("/bodywaxing") //바디왁싱
    public String bodywaxing(){
        return "pages/service/bodywaxing";
    }
    @GetMapping("/facewaxing") //페이스왁싱
    public String facewaxing(){
        return "pages/service/facewaxing";
    }
    @GetMapping("/pregnantwaxing") //페이스왁싱
    public String pregnantwaxing(){
        return "pages/service/pregnantwaxing";
    }

    //가격 카테고리
    @GetMapping("/price")
    public String price(){
        return "pages/price/price";
    }

    //커뮤니티 카테고리(이벤트/공지사항/자주묻는질문)
    @GetMapping("/event")
    public String community(){
        return "pages/community/event";
    }
    @GetMapping("/notice")
    public String notice(){
        return "pages/community/notice";
    }
    @GetMapping("/faq")
    public String faq(){
        return "pages/community/faq";
    }

    //온라인상담 카테고리
    @GetMapping("/consultings") //온라인상담
    public String consultings(){
        return "pages/consultings/consultings";
    }

    @GetMapping("/kakatalk") //카카오톡
    public String kakatalk(){
        return "pages/consultings/kakatalk";
    }

}

