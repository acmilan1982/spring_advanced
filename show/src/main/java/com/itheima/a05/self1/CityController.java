package com.itheima.a05.self1;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CityController {

    @GetMapping("/city")
    public String index(Model model) {
//        model.addAttribute("provinces", CityConst.getProvinces());
        return "city/index";
    }
}
