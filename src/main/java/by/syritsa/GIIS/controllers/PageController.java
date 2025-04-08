package by.syritsa.GIIS.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String index() {
        return "Main";
    }

    @GetMapping("/4")
    public String index4() {
        return "3dObjects";
    }

    @GetMapping("/5")
    public String index5() {
        return "Polygon";
    }
}
