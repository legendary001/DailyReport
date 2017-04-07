package com.controller.json;

import com.service.HeadLinePreLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wupeng1 on 2017/3/28.
 */
@RestController
public class HLPreLoadJsonCtl {

    @Autowired
    private HeadLinePreLoadService service;

    @RequestMapping("/headline/json/preload")
    @ResponseBody
    public String  getPreload(@RequestParam(value = "tag") String tag) throws Exception{
        return service.getPreload(tag);
    }

    @RequestMapping("/headline/json/preload/history")
    @ResponseBody
    public String  getHistoryPreload(@RequestParam(value = "tag") String tag) throws Exception{
        return service.getHistoryPreload(tag);
    }

}
