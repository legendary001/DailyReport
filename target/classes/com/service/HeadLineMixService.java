package com.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.Document;
import com.model.bean.MixAlgRankDataClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by wupeng1 on 2017/3/28.
 */
@Service
public class HeadLineMixService {

    public String getMixList(String uid) {
        if (StringUtils.isEmpty(uid)) return "";
        return new Gson().toJson(MixAlgRankDataClient.getMixRankList(uid),new TypeToken<List<Document>>() {
        }.getType());
    }

}

