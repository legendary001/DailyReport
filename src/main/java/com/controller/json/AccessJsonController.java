package com.controller.json;

import com.service.AccessService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pc on 2017/3/23.
 */
@RestController
public class AccessJsonController {

    @Autowired
    private AccessService service;



    @ResponseBody
    @RequestMapping(value = "/json/city")
    public JSONObject getCityJson(@RequestParam(value = "day", defaultValue = "20170324") String day) throws Exception{
        return service.cityStatus(day);
    }


}
