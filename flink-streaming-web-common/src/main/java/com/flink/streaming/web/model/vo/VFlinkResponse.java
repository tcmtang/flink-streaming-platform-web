package com.flink.streaming.web.model.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class VFlinkResponse {
    @JSONField(name = "errors")
    protected String errors;
    @JSONField(name = "request-id")
    protected String requestId;

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
