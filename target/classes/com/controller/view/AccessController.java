package com.controller.view;

import com.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wupeng1 on 2017/3/25.
 */
@Controller
public class AccessController {

    @Autowired
    AccessService accessService;

    @RequestMapping(value = "/city")
    public String cityStatus(HttpServletRequest httpServletRequest, Model model,
                             @RequestParam(value = "day", defaultValue = "20170324") String day) throws Exception {
        model.addAttribute("data", accessService.cityStatus(day));
        return "accessStatus";
    }

}
