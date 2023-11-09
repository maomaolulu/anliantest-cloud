package com.anliantest.job.api.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class SysJobApi implements Serializable {
    private Long jobId;

    /** 任务名称 */
    private String jobName;

    /** 任务组名 */
    private String jobGroup;

    /** 调用目标字符串 */
    private String invokeTarget;

    /** cron执行表达式 */
    private String cronExpression;

    /** cron计划策略 */
    private String misfirePolicy ;

    /** 是否并发执行（0允许 1禁止） */
    private String concurrent;

    /** 任务状态（0正常 1暂停） */
    private String status;
    /** 进度id*/
    private Long progressManageId;
    /** 操作用戶*/
    private String createBy;
}
