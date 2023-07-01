package com.flink.streaming.web.quartz;

import com.flink.streaming.web.ao.impl.JobStandaloneServerAOImpl;
import com.flink.streaming.web.common.SystemConstants;
import com.flink.streaming.web.enums.DeployModeEnum;
import com.flink.streaming.web.enums.JobConfigStatus;
import com.flink.streaming.web.model.dto.JobConfigDTO;
import com.flink.streaming.web.rpc.FlinkRestRpcAdapter;
import com.flink.streaming.web.rpc.model.JobStandaloneInfo;
import com.flink.streaming.web.service.JobConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作业自动触发Savepoint调度管理 全量一次加载任务（启动的时候）
 *
 * @author tcm
 * @Description:
 * @date 2023-02-09
 */
@Component
@Slf4j
public class JobSavepointManagerScheduler implements ApplicationRunner {

    @Autowired
    private JobConfigService jobConfigService;
    @Autowired
    private FlinkRestRpcAdapter flinkRestRpcAdapter;
    @Autowired
    private JobStandaloneServerAOImpl jobStandaloneServerAO;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("恢复自动保存点任务.");
        List<JobConfigDTO> jobConfigDTOList = jobConfigService
                .findJobConfigByStatus(JobConfigStatus.RUN.getCode());
        for (JobConfigDTO jobConfigDTO : jobConfigDTOList) {
            if (StringUtils.isNotEmpty(jobConfigDTO.getSavepointCron())
                    && StringUtils.isNotEmpty(jobConfigDTO.getSavepointTargetDirectory())) {
                registerJob(jobConfigDTO);
            } else {
                log.info("作业未配置 savepointCron 与 savepointTargetDirectory，未启用自动创建保存点.");
            }
        }
    }

    private void registerJob(JobConfigDTO jobConfigDTO) {
        if (!DeployModeEnum.STANDALONE.equals(jobConfigDTO.getDeployModeEnum())) {
            return;
        }
        JobStandaloneInfo jobStandaloneInfo = flinkRestRpcAdapter
                .getJobInfoForStandaloneByAppId(jobConfigDTO.getJobId(), jobConfigDTO.getDeployModeEnum());
        if (jobStandaloneInfo == null || StringUtils.isNotEmpty(jobStandaloneInfo.getErrors())) {
            log.error("Get Job status fail, jobStandaloneInfo={}", jobStandaloneInfo);
        } else {
            if (!SystemConstants.STATUS_RUNNING.equals(jobStandaloneInfo.getState())) {
                log.warn("Job [jobId:{},jobName:{}] status not running.not start auto savepoint scheduler.",
                        jobConfigDTO.getJobId(), jobConfigDTO.getJobName());
            }
            jobStandaloneServerAO.registerJobSavepointTrigger(jobConfigDTO);
        }
    }
}
