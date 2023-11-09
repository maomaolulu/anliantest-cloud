package com.anliantest.data.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.data.domain.dto.EquipWarehouseRecordDto;
import com.anliantest.data.entity.EquipBorrowingRecord;
import com.anliantest.data.entity.EquipExternal;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.anliantest.data.service.EquipBorrowingRecordService;
import com.anliantest.data.service.EquipExternalService;
import io.swagger.annotations.Api;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.List;

/**
 * 设备借用记录
 * @author zhanghao
 * @date 2023-08-04
 * @desc : 设备借用记录
 */
@RestController
@Api(tags = "设备借用记录")
@RequestMapping("/equip_borrowing_record")
public class EquipBorrowingRecordController extends BaseController {


    private final EquipBorrowingRecordService equipBorrowingRecordService;
    private final EquipExternalService equipExternalService;
    @Autowired
    public EquipBorrowingRecordController(EquipBorrowingRecordService equipBorrowingRecordService, EquipExternalService equipExternalService) {
        this.equipBorrowingRecordService = equipBorrowingRecordService;
        this.equipExternalService = equipExternalService;
    }




    /**
     * 设备借出列表
     * @param equipWarehouseRecordDto
     * @return
     */
    @GetMapping("lendListPage")
    public TableDataInfo listPage(EquipWarehouseRecordDto equipWarehouseRecordDto){


        startPage();
        List<EquipWarehouseRecord> equipWarehouseRecords = equipBorrowingRecordService.lendListPage(equipWarehouseRecordDto);
        return getDataTable(equipWarehouseRecords);
    }


    /**
     * 设备借用记录分页
     * @param equipBorrowingRecord
     * @return
     */
    @GetMapping("listPage")
    public TableDataInfo listPage(EquipBorrowingRecord equipBorrowingRecord){
        startPage();
        List<EquipBorrowingRecord> equipBorrowingRecords = equipBorrowingRecordService.listPage(equipBorrowingRecord);
        return getDataTable(equipBorrowingRecords);
    }

    /**
     * 外部设备借入记录分页
     * @param equipBorrowingRecord
     * @return
     */
    @GetMapping("externalListPage")
    public TableDataInfo externalListPage(EquipBorrowingRecord equipBorrowingRecord){
        startPage();
        List<EquipBorrowingRecord> equipBorrowingRecords = equipBorrowingRecordService.externalListPage(equipBorrowingRecord);
        return getDataTable(equipBorrowingRecords);
    }



    /**
     * 新增设备借用记录
     * @param equipBorrowingRecord
     */
    @PostMapping("save")
    @Log(title = "新增设备借用记录", businessType = BusinessType.INSERT)
    public R save(@RequestBody EquipBorrowingRecord equipBorrowingRecord ){
        equipBorrowingRecord.setCreateById(SecurityUtils.getUserId());
        equipBorrowingRecord.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getNickName());
        equipBorrowingRecord.setId(null);
        equipBorrowingRecord.setActualEndTime(equipBorrowingRecord.getEndTime());
         equipBorrowingRecordService.save(equipBorrowingRecord);
        return R.ok("新增成功");
    }


    /**
     * 撤销归还
     * @param equipBorrowingRecord
     * @return
     */
    @Log(title = "撤销归还", businessType = BusinessType.UPDATE)
    @PostMapping("revokeEquipBorrowingRecord")
    public R revokeEquipBorrowingRecord(@RequestBody  EquipBorrowingRecord equipBorrowingRecord){
        equipBorrowingRecord.setStatus(3);
        equipBorrowingRecordService.updateById(equipBorrowingRecord);
        return R.ok("撤销归还成功");
    }
//    /**
//     * 提前归还
//     * @param equipBorrowingRecord
//     * @return
//     */
//    @Log(title = "修改设备借用记录", businessType = BusinessType.UPDATE)
//    @PostMapping("update")
//    public R update(@RequestBody  EquipBorrowingRecord equipBorrowingRecord){
//
//        equipBorrowingRecordService.updateById(equipBorrowingRecord);
//        return R.ok("修改成功");
//    }

   /**
     * 删除设备借用记录
     */
    @Log(title = "删除设备借用记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/del")
    public R remove(@RequestBody List<Long> roleIds) {

        equipBorrowingRecordService.removeByIds(roleIds);
        return R.ok("删除成功");
    }




}
