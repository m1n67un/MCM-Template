package com.mg.api.sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/index.html";
    }

    @GetMapping("/access/test1")
    public String accessTest1() {
        return "/accessTest/test1.html";
    }

    @GetMapping("/access/test2")
    public String accessTest2() {
        return "/accessTest/test2.html";
    }

    @GetMapping("/access/test3")
    public String accessTest3() {
        return "/accessTest/test3.html";
    }

}
