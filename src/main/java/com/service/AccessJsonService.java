package com.service;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pc on 2017/3/23.
 */
@Service
public class AccessJsonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JSONObject cityStatus(String day) {
        JSONObject jsonObject = new JSONObject();

        String queryType = "SELECT city,count FROM access_city WHERE day = '" + day + "' ORDER BY count DESC LIMIT 10;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("name", resultSet.getString("city"));
            jsonObject1.put("value", resultSet.getString("count"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }


}
