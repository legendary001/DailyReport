package com.service;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;

/**
 * Created by pc on 2017/3/23.
 */
@Service
public class AccessService {

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

    public JSONObject lastdocStatus(String day) {
        JSONObject jsonObject = new JSONObject();
        String queryType = "SELECT operation,count FROM access_lastdoc WHERE day = '" + day + "' ORDER BY count DESC;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("operation", resultSet.getString("operation"));
            jsonObject1.put("value", resultSet.getString("count"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }


    public JSONObject lastdocPullStatus(String day) {
        JSONObject jsonObject = new JSONObject();
        String queryType = "SELECT operation,count FROM access_lastdoc_pull_operation WHERE day = '" + day + "' ORDER BY count DESC;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("operation", resultSet.getString("operation"));
            jsonObject1.put("value", resultSet.getString("count"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }

    public JSONObject netStatus(String day) {
        JSONObject jsonObject = new JSONObject();
        String queryType = "SELECT operation,count FROM access_net WHERE day = '" + day + "' ORDER BY count DESC;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("operation", resultSet.getString("operation"));
            jsonObject1.put("value", resultSet.getString("count"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }

    public JSONObject totalOperationStatus(String day) {
        JSONObject jsonObject = new JSONObject();
        String queryType = "SELECT operation,count,percent FROM access_total WHERE day = '" + day + "' ORDER BY count DESC;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("operation", resultSet.getString("operation"));
            jsonObject1.put("value", resultSet.getString("count"));
            jsonObject1.put("percent", resultSet.getString("percent"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }


    public JSONObject resultLoadReasonStatus(String day) {
        JSONObject jsonObject = new JSONObject();
        String queryType = "SELECT operation,count,percent FROM result_load_reason WHERE day = '" + day + "' ORDER BY count DESC;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("operation", resultSet.getString("operation"));
            jsonObject1.put("value", resultSet.getString("count"));
            jsonObject1.put("percent", resultSet.getString("percent"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }

    public JSONObject resultLoadReasonWeekendStatus(String day, String operation) {
        JSONObject jsonObject = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String lastWeekendDay = null;
        try {
            Date date = dateFormat.parse(day);
            Date date1 = new Date(date.getTime() - 24 * 60 * 60 * 7 * 1000L);
            lastWeekendDay = dateFormat.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String queryType = "SELECT operation,count,day FROM result_load_reason WHERE operation = '" + operation + "' AND day between '" + lastWeekendDay +"' AND '" + day +"' ORDER BY day;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("day", resultSet.getString("day"));
            jsonObject1.put("operation", resultSet.getString("operation"));
            jsonObject1.put("value", resultSet.getString("count"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }



    public JSONObject resultLoadSourceStatus(String day) {
        JSONObject jsonObject = new JSONObject();
        String queryType = "SELECT operation,count,percent FROM result_load_source WHERE day = '" + day + "' ORDER BY count DESC;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("operation", resultSet.getString("operation"));
            jsonObject1.put("value", resultSet.getString("count"));
            jsonObject1.put("percent", resultSet.getString("percent"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }

    public JSONObject resultLoadSourceWeekendStatus(String day, String operation) {
        JSONObject jsonObject = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String lastWeekendDay = null;
        try {
            Date date = dateFormat.parse(day);
            Date date1 = new Date(date.getTime() - 24 * 60 * 60 * 7 * 1000L);
            lastWeekendDay = dateFormat.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String queryType = "SELECT operation,count,day FROM result_load_source WHERE operation = '" + operation + "' AND day between '" + lastWeekendDay +"' AND '" + day +"' ORDER BY day;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("day", resultSet.getString("day"));
            jsonObject1.put("operation", resultSet.getString("operation"));
            jsonObject1.put("value", resultSet.getString("count"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }


    public JSONObject timeConsumeStatus(String day) {
        JSONObject jsonObject = new JSONObject();
        String queryType = "SELECT operation,count,percent FROM timeconsume_timeout WHERE day = '" + day + "' ORDER BY count DESC;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("operation", resultSet.getString("operation"));
            jsonObject1.put("value", resultSet.getString("count"));
            jsonObject1.put("percent", resultSet.getString("percent"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }



    public JSONObject timeConsumeWeekendStatus(String day, String operation) {
        JSONObject jsonObject = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String lastWeekendDay = null;
        try {
            Date date = dateFormat.parse(day);
            Date date1 = new Date(date.getTime() - 24 * 60 * 60 * 7 * 1000L);
            lastWeekendDay = dateFormat.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String queryType = "SELECT operation,percent,day FROM timeconsume_timeout WHERE operation = '" + operation + "' AND day between '" + lastWeekendDay +"' AND '" + day +"' ORDER BY day;";
        List<JSONObject> listType = jdbcTemplate.query(queryType, (resultSet, num) -> {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("day", resultSet.getString("day"));
            jsonObject1.put("operation", resultSet.getString("operation"));
            jsonObject1.put("percent", resultSet.getString("percent"));
            return jsonObject1;
        });
        jsonObject.put("result", listType);
        return jsonObject;
    }
}

