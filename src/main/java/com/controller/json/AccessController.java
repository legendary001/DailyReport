package com.controller.json;

import com.service.AccessJsonService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pc on 2017/3/23.
 */
@RestController
public class AccessController {

    @Autowired
    private AccessJsonService service;

//    @RequestMapping(value = "/")
//    public String index(HttpServletRequest httpServletRequest, Model model) throws Exception{
//        return "about";
//    }

    @RequestMapping(value = "/json/")
    public JSONObject getCityJson() throws Exception{
        return service.cityStatus();
    }


}
