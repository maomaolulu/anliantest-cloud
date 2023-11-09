package com.anliantest.data.controller;

import cn.hutool.core.util.StrUtil;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.data.entity.EquipCategory;
import com.anliantest.data.entity.EquipPurchaseRecord;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.anliantest.data.service.EquipCategoryService;
import com.anliantest.data.service.EquipPurchaseRecordService;
import com.anliantest.data.service.EquipWarehouseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 仪器设备一期
 *
 * @Author yrb
 * @Date 2023/8/1 14:58
 * @Version 1.0
 * @Description 仪器设备控制层
 */
@RestController
@RequestMapping("/equip")
@Slf4j
public class EquipController extends BaseController {
    private final EquipWarehouseRecordService equipWarehouseRecordService;
    private final EquipPurchaseRecordService equipPurchaseRecordService;
    private final EquipCategoryService equipCategoryService;

    public EquipController(EquipWarehouseRecordService equipWarehouseRecordService,
                           EquipPurchaseRecordService equipPurchaseRecordService,
                           EquipCategoryService equipCategoryService) {
        this.equipWarehouseRecordService = equipWarehouseRecordService;
        this.equipPurchaseRecordService = equipPurchaseRecordService;
        this.equipCategoryService = equipCategoryService;
    }

    /**
     * 仪器设备入库记录列表
     *
     * @param equipWarehouseRecord 入库信息
     * @return 集合
     */
    @GetMapping("/list/warehouse")
    public TableDataInfo list(EquipWarehouseRecord equipWarehouseRecord) {
        startPage();
        List<EquipWarehouseRecord> list = equipWarehouseRecordService.list(equipWarehouseRecord);
        return getDataTable(list);
    }

    /**
     * 采购信息列表
     *
     * @param equipPurchaseRecord 采购记录
     * @return 集合
     */
    @GetMapping("/list/purchase")
    public TableDataInfo list(EquipPurchaseRecord equipPurchaseRecord) {
        startPage();
        List<EquipPurchaseRecord> list = equipPurchaseRecordService.list(equipPurchaseRecord);
        return getDataTable(list);
    }

    /**
     * 仪器设备入库
     *
     * @param equipWarehouseRecord 入库信息
     * @return result
     */
    @Log(title = "仪器设备入库", businessType = BusinessType.UPDATE)
    @PostMapping("/add/warehouse")
    public R add(@RequestBody EquipWarehouseRecord equipWarehouseRecord) {
        try {
            if (StrUtil.isBlank(equipWarehouseRecord.getEquipCode())) {
                return R.fail("设备编号不能为空");
            }
            if (equipWarehouseRecordService.checkEuipCodeUnique(equipWarehouseRecord.getEquipCode()) != 0) {
                return R.fail("设备编号已存在");
            }
            if (equipWarehouseRecord.getWarehouseType() == null) {
                return R.fail("入库类型不能为空");
            }
            if (equipWarehouseRecord.getWarehouseType() == 1 && equipWarehouseRecord.getOldId() == null) {
                return R.fail("采购入库需要传入采购记录主键ID！");
            }
            if (equipWarehouseRecordService.add(equipWarehouseRecord) != 0) {
                return R.ok("仪器设备入库成功");
            }
            return R.fail("仪器设备入库失败");
        } catch (Exception e) {
            logger.error("仪器设备入库发生异常===============" + e);
            return R.fail("仪器设备入库失败");
        }
    }

    /**
     * OA采购信息入库
     *
     * @param equipPurchaseRecord 采购信息
     * @return result
     */
    @Log(title = "采购信息入库", businessType = BusinessType.UPDATE)
    @PostMapping("/add/purchase")
    public int add(@RequestBody EquipPurchaseRecord equipPurchaseRecord) {
        try {
            if (equipPurchaseRecord.getUserId() == null) {
                return 3;
            }
            if (equipPurchaseRecordService.add(equipPurchaseRecord)) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            log.error("采购信息入库异常============" + e.getMessage());
            return 2;
        }
    }


    /**
     * 获取应检定设备列表
     *
     * @param equipWarehouseRecord
     * @return
     */
    @GetMapping("/list/verification")
    public TableDataInfo getVerificationList(EquipWarehouseRecord equipWarehouseRecord) {
        startPage();
        List<EquipWarehouseRecord> list = equipWarehouseRecordService.getVerificationList(equipWarehouseRecord);
        return getDataTable(list);
    }


    /**
     * 设备详情
     *
     * @param id
     * @return
     */
    @GetMapping("info/warehouse")
    public R getInfo(Long id) {
        if (id != null) {
            EquipWarehouseRecord equipWarehouseRecord = equipWarehouseRecordService.getInfo(id);
            return R.ok(equipWarehouseRecord);
        } else {
            logger.error("未传id，设备查询失败");
            return R.fail("查询失败");
        }
    }

    /**
     * 更新标签打印状态
     *
     * @param equipCode 设备编号
     * @return result
     */
    @Log(title = "更新标签打印状态", businessType = BusinessType.UPDATE)
    @PostMapping("/updatePrintLabel/purchase")
    public int updatePrintLabel(@RequestBody String equipCode) {
        try {
            if (StrUtil.isBlank(equipCode)) {
                return 0;
            }
            return equipWarehouseRecordService.updatePrintLabel(equipCode);
        } catch (Exception e) {
            log.error("更新标签打印状态异常" + e);
            return 0;
        }
    }

    /**
     * 添加仪器设备信息
     *
     * @param equipCategory 仪器类型信息
     * @return 结果
     */
    @Log(title = "添加仪器类型信息", businessType = BusinessType.UPDATE)
    @PostMapping("/add/category")
    public R add(@RequestBody EquipCategory equipCategory) {
        try {
            if (StrUtil.isBlank(equipCategory.getCategoryName())) {
                return R.fail("仪器类型名称不能为空");
            }
            int i = equipCategoryService.add(equipCategory);
            if (i == 2) {
                return R.fail("仪器类型名称已存在");
            }
            return i == 0 ? R.fail("新增失败") : R.ok(null, "新增成功");
        } catch (Exception e) {
            log.error("新增仪器设备信息异常" + e);
            return R.fail("新增失败");
        }
    }

    /**
     * 编辑仪器类型信息
     *
     * @param equipCategory 仪器类型信息
     * @return 结果
     */
    @Log(title = "编辑仪器类型信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit/category")
    public R edit(@RequestBody EquipCategory equipCategory) {
        try {
            if (equipCategory.getId() == null) {
                return R.fail("仪器类型id不能为空");
            }
            if (StrUtil.isBlank(equipCategory.getCategoryName())) {
                return R.fail("仪器类型名称不能为空");
            }
            int i = equipCategoryService.edit(equipCategory);
            if (i == 2) {
                return R.fail("仪器类型名称已存在");
            }
            return i == 0 ? R.fail("编辑失败") : R.ok(null, "编辑成功");
        } catch (Exception e) {
            log.error("编辑仪器设备信息异常" + e);
            return R.fail("编辑失败");
        }
    }

    /**
     * 删除仪器类型信息
     *
     * @param id 主键ID
     * @return 结果
     */
    @Log(title = "删除仪器类型信息", businessType = BusinessType.UPDATE)
    @DeleteMapping("/delete/category/{id}")
    public R delete(@PathVariable("id") Long id) {
        try {
            return equipCategoryService.delete(id) == 0 ? R.fail("当前类型已绑定设备，无法删除") : R.ok(null, "删除成功");
        } catch (Exception e) {
            log.error("删除仪器设备信息异常" + e);
            return R.fail("删除失败");
        }
    }

    /**
     * 获取仪器类型一级类目下的子类下拉列表（单个一级类目）
     *
     * @param equipCategory 仪器设备信息
     * @return 集合
     */
    @GetMapping("/getDropdownList/category")
    public List<EquipCategory> getDropdownList(EquipCategory equipCategory) {
        try {
            if (StrUtil.isBlank(equipCategory.getCategoryName())) {
                return new ArrayList<>();
            }
            return equipCategoryService.getDropdownList(equipCategory);
        } catch (Exception e) {
            log.error("获取仪器类型下拉列表异常=====" + e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取仪器类型列表
     *
     * @param equipCategory 仪器设备信息
     * @return 集合
     */
    @GetMapping("/list/category")
    public R list(EquipCategory equipCategory) {
        try {
            return R.ok(equipCategoryService.getList(equipCategory));
        } catch (Exception e) {
            log.error("获取仪器类型多级列表异常=====" + e);
            return R.fail("获取仪器类型列表失败!");
        }
    }
}
