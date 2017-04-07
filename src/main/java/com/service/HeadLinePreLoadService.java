package com.service;

import com.util.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by wupeng1 on 2017/3/28.
 */
@Service
public class HeadLinePreLoadService {

    public String getPreload(String tag) {
        String value = RedisUtil.get("10.90.4.17", 6379, 12, tag);
        if(StringUtils.isEmpty(value)) return "";
        return value;
    }

    public String getHistoryPreload(String tag) {
        String value = RedisUtil.get("10.90.4.17", 6379, 13, tag);
        if(StringUtils.isEmpty(value)) return "";
        return value;
    }

}
