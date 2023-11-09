package com.anliantest.project.controller;

import cn.hutool.core.collection.CollUtil;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.project.domain.dto.CustomAdvanceDto;
import com.anliantest.project.domain.vo.HistoryAdvanceVo;
import com.anliantest.project.entity.CustomAdvanceRecordEntity;
import com.anliantest.project.entity.CustomAdvanceTaskEntity;
import com.anliantest.project.service.CustomAdvanceRecordService;
import com.anliantest.project.service.CustomAdvanceTaskService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务跟进
 *
 * @Author yrb
 * @Date 2023/8/21 17:32
 * @Version 1.0
 * @Description 任务跟进
 */
@RestController
@RequestMapping("/advance")
@Slf4j
public class CustomAdvanceController extends BaseController {
    private final CustomAdvanceRecordService customAdvanceRecordService;
    private final CustomAdvanceTaskService customAdvanceTaskService;

    public CustomAdvanceController(CustomAdvanceRecordService customAdvanceRecordService,
                                   CustomAdvanceTaskService customAdvanceTaskService) {
        this.customAdvanceRecordService = customAdvanceRecordService;
        this.customAdvanceTaskService = customAdvanceTaskService;
    }

    /**
     * 新增跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return result
     */
    @Log(title = "新增跟进记录", businessType = BusinessType.INSERT)
    @PutMapping("/record/save")
    public R save(@RequestBody CustomAdvanceRecordEntity customAdvanceRecord) {
        try {
            if (customAdvanceRecord.getTaskId() == null) {
                return R.fail("要跟进的任务ID不能为空");
            }
            if (customAdvanceRecordService.add(customAdvanceRecord) > 0) {
                return R.ok(null, "新增成功");
            }
            return R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增跟进记录异常======" + e);
            return R.fail("新增跟进记录异常");
        }
    }

    /**
     * 修改任务信息
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @Log(title = "修改任务信息", businessType = BusinessType.UPDATE)
    @PostMapping("/task/update")
    public R update(@RequestBody CustomAdvanceTaskEntity customAdvanceTask) {
        try {
            if (customAdvanceTask.getId() == null) {
                if (customAdvanceTask.getAdvanceResult() != null && customAdvanceTask.getTaskId() != null) {
                    customAdvanceTask.setId(customAdvanceTask.getTaskId());
                }else{
                    return R.fail("任务ID不能为空");
                }
            }
            if (customAdvanceTaskService.modify(customAdvanceTask) > 0) {
                return R.ok(null, "提交成功");
            }
            return R.fail("提交失败");
        } catch (Exception e) {
            log.error("提交跟进记录异常======" + e);
            return R.fail("提交跟进记录异常");
        }
    }

    /**
     * 人员替换
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @Log(title = "人员替换", businessType = BusinessType.UPDATE)
    @PostMapping("/task/replace")
    public R replace(@RequestBody CustomAdvanceTaskEntity customAdvanceTask) {
        try {
            if (CollUtil.isEmpty(customAdvanceTask.getTasksList())) {
                return R.fail("要替换的任务ID不能为空");
            }
            if (customAdvanceTask.getUserId() == null) {
                return R.fail("请选择要替换的人员");
            }
            if (customAdvanceTaskService.replaceUserBatch(customAdvanceTask) > 0) {
                return R.ok(null, "替换成功");
            }
            return R.fail("替换失败");
        } catch (Exception e) {
            log.error("替换任务跟进人员异常======" + e);
            return R.fail("替换任务跟进人员异常");
        }
    }

    /**
     * 查询跟进任务列表
     *
     * @param dto 查询条件
     * @return result
     */
    @Log(title = "查询跟进任务列表")
    @GetMapping("/record/listTasks")
    public TableDataInfo listTasks(CustomAdvanceDto dto) {
        startPage();
        return getDataTable(customAdvanceTaskService.listTasks(dto));
    }

    /**
     * 批量分配
     *
     * @param customAdvanceTask 任务信息
     * @return 结果
     */
    @Log(title = "批量分配", businessType = BusinessType.UPDATE)
    @PostMapping("/task/allocation")
    public R allocationBatch(@RequestBody CustomAdvanceTaskEntity customAdvanceTask) {
        try {
            if (customAdvanceTask.getUserId() == null) {
                return R.fail("要分配的业务员ID不能为空");
            }
            if (CollUtil.isEmpty(customAdvanceTask.getTasksList())) {
                return R.fail("要分配的任务为空");
            }
            if (customAdvanceTaskService.allocationBatch(customAdvanceTask) == 0) {
                return R.fail("任务分配失败");
            }
            return R.ok(null, "任务分配成功");
        } catch (Exception e) {
            log.error("任务分配发生异常===========" + e);
            return R.fail("任务分配发生异常");
        }
    }

    /**
     * 跟进详情
     *
     * @param customAdvanceRecord 跟进记录
     * @return result
     */
    @Log(title = "跟进详情")
    @GetMapping("/getDetail")
    public R getDetail(CustomAdvanceRecordEntity customAdvanceRecord) {
        try {
            if (customAdvanceRecord.getTaskId() == null) {
                return R.fail("请输入要查询的任务ID");
            }
            startPage();
            return R.ok(customAdvanceRecordService.getDetail(customAdvanceRecord));
        } catch (Exception e) {
            return R.fail("获取跟进详情异常");
        }
    }

    /**
     * 编辑跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return result
     */
    @Log(title = "编辑跟进记录", businessType = BusinessType.UPDATE)
    @PutMapping("/record/edit")
    public R edit(@RequestBody CustomAdvanceRecordEntity customAdvanceRecord) {
        try {
            if (customAdvanceRecord.getId() == null) {
                return R.fail("记录ID不能为空");
            }
            if (customAdvanceRecord.getTaskId() == null) {
                return R.fail("任务ID不能为空");
            }
            if (customAdvanceRecordService.updateAdvanceInfo(customAdvanceRecord) > 0) {
                return R.ok(null, "编辑成功");
            }
            return R.fail("编辑失败");
        } catch (Exception e) {
            log.error("编辑跟进记录异常======" + e);
            return R.fail("编辑跟进记录异常");
        }
    }

    /**
     * 删除跟进记录
     *
     * @param id 跟进记录id
     * @return result
     */
    @Log(title = "删除跟进记录", businessType = BusinessType.UPDATE)
    @DeleteMapping("/record/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        try {
            if (id == null) {
                return R.fail("跟进记录id不能为空");
            }
            return customAdvanceRecordService.delete(id) == 1 ? R.ok(null, "删除成功") : R.fail("删除失败");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 历史跟进
     */
    @GetMapping("/history")
    public TableDataInfo historyAdvance(Long companyId) {
        startPage();
        List<HistoryAdvanceVo> list = customAdvanceRecordService.historyAdvance(companyId);
        return getDataTable(list);
    }


    /**
     * 业务员主动释放
     */
    @PostMapping("/releaseCompany")
    @ApiOperation("业务员主动释放")
    public R<String> releaseCompany(@RequestBody CustomAdvanceTaskEntity dto){
        if (dto.getId() == null || dto.getCustomId() == null){
            return R.fail("客户id或跟进任务id为null");
        }
        return customAdvanceTaskService.releaseCompany(dto) ? R.ok("操作成功") : R.fail("操作失败");
    }

    /**
     * 定时任务远程调用-定时释放客户至公海
     */
    @PostMapping("/releaseCompanyJob")
    @ApiOperation("定时释放客户至公海")
    public void releaseCompanyJob(){
        customAdvanceTaskService.releaseCompanyJob();
    }
}
