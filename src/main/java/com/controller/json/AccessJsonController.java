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
    @RequestMapping(value = "/json/access/city")
    public JSONObject getAccessCityJson(@RequestParam(value = "day", defaultValue = "20170324") String day) throws Exception{
        return service.cityStatus(day);
    }

    @ResponseBody
    @RequestMapping(value = "/json/access/lastdoc")
    public JSONObject getAccesslastdocJson(@RequestParam(value = "day", defaultValue = "20170324") String day) throws Exception{
        return service.lastdocStatus(day);
    }

    @ResponseBody
    @RequestMapping(value = "/json/access/lastdocpull")
    public JSONObject getAccessLastdocPullJson(@RequestParam(value = "day", defaultValue = "20170326") String day) throws Exception{
        return service.lastdocPullStatus(day);
    }


    @ResponseBody
    @RequestMapping(value = "/json/access/net")
    public JSONObject getAccessNetJson(@RequestParam(value = "day", defaultValue = "20170324") String day) throws Exception{
        return service.netStatus(day);
    }

    @ResponseBody
    @RequestMapping(value = "/json/access/total")
    public JSONObject getAccessTotalJson(@RequestParam(value = "day", defaultValue = "20170324") String day) throws Exception{
        return service.totalOperationStatus(day);
    }


    @ResponseBody
    @RequestMapping(value = "/json/access/loadreason")
    public JSONObject getResultLoadReasonJson(@RequestParam(value = "day", defaultValue = "20170324") String day) throws Exception{
        return service.resultLoadReasonStatus(day);
    }

    @ResponseBody
    @RequestMapping(value = "/json/access/loadreason/weekend")
    public JSONObject getResultLoadReasonWeekendJson(@RequestParam(value = "day", defaultValue = "20170324") String day, String operation) throws Exception{
        return service.resultLoadReasonWeekendStatus(day, operation);
    }

    @ResponseBody
    @RequestMapping(value = "/json/access/loadsource")
    public JSONObject getResultLoadSourceJson(@RequestParam(value = "day", defaultValue = "20170324") String day) throws Exception{
        return service.resultLoadSourceStatus(day);
    }


    @ResponseBody
    @RequestMapping(value = "/json/access/loadsource/weekend")
    public JSONObject getResultLoadSourceWeekendJson(@RequestParam(value = "day", defaultValue = "20170324") String day, String operation) throws Exception{
        return service.resultLoadSourceWeekendStatus(day, operation);
    }


    @ResponseBody
    @RequestMapping(value = "/json/access/timeconsume")
    public JSONObject getTimeConsumeJson(@RequestParam(value = "day", defaultValue = "20170324") String day) throws Exception{
        return service.timeConsumeStatus(day);
    }

    @ResponseBody
    @RequestMapping(value = "/json/access/timeconsume/weekend")
    public JSONObject getTimeConsumeJson(@RequestParam(value = "day", defaultValue = "20170324") String day, String operation) throws Exception{
        return service.timeConsumeWeekendStatus(day, operation);
    }
}
