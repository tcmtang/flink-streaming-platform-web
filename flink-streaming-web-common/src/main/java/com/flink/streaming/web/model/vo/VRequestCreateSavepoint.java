package com.flink.streaming.web.model.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class VRequestCreateSavepoint {
    @JSONField(name = "target-directory")
    String targetDirectory;
    @JSONField(name = "cancel-job")
    boolean cancelJob;

    /*//value from: new TriggerId().toHexString()
    // flink 1.15后支持
    @JSONField(name = "triggerId")
    String triggerId;
    // flink 1.15后支持
    @JSONField(name = "formatType")
    JobSavepointFormatType formatType;*/

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public boolean isCancelJob() {
        return cancelJob;
    }

    public void setCancelJob(boolean cancelJob) {
        this.cancelJob = cancelJob;
    }

    /*public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public JobSavepointFormatType getFormatType() {
        return formatType;
    }

    public void setFormatType(JobSavepointFormatType formatType) {
        this.formatType = formatType;
    }*/
}
