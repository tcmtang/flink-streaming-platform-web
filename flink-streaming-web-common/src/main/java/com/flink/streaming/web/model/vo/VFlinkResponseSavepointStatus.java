package com.flink.streaming.web.model.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flink.streaming.web.enums.JobSavepointStatus;

public class VFlinkResponseSavepointStatus extends VFlinkResponse {

    JobSavepointStatus status;
    String location;

    public JobSavepointStatus getStatus() {
        return status;
    }

    public void setStatus(JobSavepointStatus status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //flink 1.13: {"status":{"id":"COMPLETED"},"operation":{"location":"file:/opt/flink/test/savepoint-5d176f-a81911b808ad"}}
    public static VFlinkResponseSavepointStatus jsonSerialize(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        VFlinkResponseSavepointStatus status = new VFlinkResponseSavepointStatus();
        status.setStatus(
                JobSavepointStatus.valueOf(jsonObject.getJSONObject("status").getString("id"))
        );
        status.setLocation(jsonObject.getJSONObject("operation").getString("location"));
        return status;
    }
}
