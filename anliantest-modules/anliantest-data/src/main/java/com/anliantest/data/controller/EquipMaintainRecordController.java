package com.anliantest.data.controller;

import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.domain.AjaxResult;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.data.domain.vo.EquipMaintainVo;
import com.anliantest.data.domain.vo.PeriodCheckVo;
import com.anliantest.data.entity.EquipMaintainRecord;
import com.anliantest.data.service.EquipMaintainRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备维修记录-控制层
 * @author: liyongqiang
 * @create: 2023-08-07 10:59
 */
@Slf4j
@RestController
@RequestMapping("/equip/repair")
public class EquipMaintainRecordController extends BaseController {

    private final EquipMaintainRecordService equipMaintainRecordService;

    public EquipMaintainRecordController(EquipMaintainRecordService equipMaintainRecordService) {
        this.equipMaintainRecordService = equipMaintainRecordService;
    }

    /**
     * 设备维修-列表
     */
    @GetMapping("/list")
    public TableDataInfo list(EquipMaintainVo equipMaintainVo){
        startPage();
        List<EquipMaintainVo> list = equipMaintainRecordService.selectEquipMaintainList(equipMaintainVo);
        return getDataTable(list);
    }

    /**
     * 在用设备列表
     */
    @GetMapping("/use")
    public AjaxResult equipUseList() {
        return AjaxResult.success(equipMaintainRecordService.selectEquipUseList());
    }

    /**
     * 新增
     */
    @Log(title = "新增-维修记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EquipMaintainRecord maintainRecord) {
        return toAjax(equipMaintainRecordService.insertEquipMaintainRecord(maintainRecord));
    }

    /**
     * 删除
     */
    @Log(title = "删除-维修记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}/{equipId}")
    public AjaxResult remove(@PathVariable Long id, @PathVariable Long equipId) {
        return toAjax(equipMaintainRecordService.deleteEquipMaintainRecordById(id, equipId));
    }

    /**
     * 编辑
     */
    @Log(title = "编辑-维修记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EquipMaintainVo equipMaintainVo) {
        return toAjax(equipMaintainRecordService.updateEquipMaintainRecord(equipMaintainVo));
    }

    /**
     * 完成维修
     */
    @Log(title = "设备-完成维修", businessType = BusinessType.UPDATE)
    @PostMapping("/complete")
    public AjaxResult complete(@RequestBody EquipMaintainVo equipMaintainVo) {
        return toAjax(equipMaintainRecordService.completeRepair(equipMaintainVo));
    }

    /**
     * 维修详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(equipMaintainRecordService.selectInfoById(id));
    }


    /**
     * 期间核查列表
     */
    @GetMapping("/check")
    public TableDataInfo periodCheck(PeriodCheckVo periodCheckVo) {
        startPage();
        List<PeriodCheckVo> list = equipMaintainRecordService.selectPeriodCheckList(periodCheckVo);
        return getDataTable(list);
    }

    // Todo：期间核查，企微消息提醒！（仅推送每日待核查的设备？）

    /**
     * 已核查
     */
    @GetMapping("/have")
    public AjaxResult haveChecked(Long equipId) {
        return toAjax(equipMaintainRecordService.haveChecked(equipId));
    }


}
