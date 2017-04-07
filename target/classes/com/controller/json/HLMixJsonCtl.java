package com.controller.json;

import com.service.HeadLineMixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wupeng1 on 2017/3/28.
 */
@RestController
public class HLMixJsonCtl {

    @Autowired
    private HeadLineMixService service;

    @ResponseBody
    @RequestMapping(value = "/headline/json/mix")
    public String getMixList(@RequestParam(value = "uid") String uid) throws Exception{
        return service.getMixList(uid);
    }
}
