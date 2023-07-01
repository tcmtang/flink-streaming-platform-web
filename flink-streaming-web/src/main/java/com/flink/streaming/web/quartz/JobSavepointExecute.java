package com.flink.streaming.web.quartz;

import com.flink.streaming.web.ao.JobServerAO;
import com.flink.streaming.web.common.MessageConstants;
import com.flink.streaming.web.enums.DeployModeEnum;
import com.flink.streaming.web.enums.JobConfigStatus;
import com.flink.streaming.web.exceptions.BizException;
import com.flink.streaming.web.factory.ApplicationContextProvider;
import com.flink.streaming.web.factory.JobServerAOFactory;
import com.flink.streaming.web.model.dto.JobConfigDTO;
import com.flink.streaming.web.service.JobConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 作业 Savepoint 定时任务
 *
 * @author tcm
 * @Description:
 * @date 2023-02-08
 */
@Slf4j
@Component
@Lazy
public class JobSavepointExecute implements Job {
    private JobConfigService jobConfigService = ApplicationContextProvider
            .getBean(JobConfigService.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("开始执行作业自动 Savepoint");
        Long id = null;
        try {
            id = (Long) context.getJobDetail().getJobDataMap().get("id");
            //String jobName = (String) context.getJobDetail().getJobDataMap().get("jobName");
            JobConfigDTO jobConfigDTO = getJobConfigDTO(id);
            JobServerAO jobServerAO = this.getJobServerAO(jobConfigDTO);

            checkAutoSavepoint(jobConfigDTO);
            jobServerAO.savepoint(jobConfigDTO.getId());
        } catch (Exception e) {
            log.error("JobExecute-execute is error id={}", id, e);
        }
    }

    private void checkAutoSavepoint(JobConfigDTO jobConfigDTO) {
        if (DeployModeEnum.STANDALONE.equals(jobConfigDTO.getDeployModeEnum())) {
            return;
        }
        log.error(MessageConstants.MESSAGE_013, jobConfigDTO);
        throw new BizException(MessageConstants.MESSAGE_013);
    }

    private JobServerAO getJobServerAO(JobConfigDTO jobConfigDTO) {
        return JobServerAOFactory.getJobServerAO(jobConfigDTO.getDeployModeEnum());
    }

    private JobConfigDTO getJobConfigDTO(Long id) {
        JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);
        if (jobConfigDTO == null) {
            log.error("不是批任务或者任务不存在 不能执行定时调度 id={} jobConfigDTO={}", id, jobConfigDTO);
            throw new NullPointerException("getJobServerAO is null");
        } else if (StringUtils.isEmpty(jobConfigDTO.getSavepointCron())) {
            log.error("作业未设置 SavepointCron,id={} jobConfigDTO={}", id, jobConfigDTO);
            throw new NullPointerException("getJobServerAO is null");
        } else if (jobConfigDTO.getStatus() != JobConfigStatus.RUN) {
            throw new RuntimeException("Job status is not running.");
        }
        return jobConfigDTO;
    }
}
