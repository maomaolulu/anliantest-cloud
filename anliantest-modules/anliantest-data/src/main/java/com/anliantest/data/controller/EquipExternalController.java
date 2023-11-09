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
import com.anliantest.system.api.model.LoginUser;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 外部设备
 * @author zhanghao
 * @date 2023-08-04
 * @desc : 外部设备
 */
@RestController
@Api(tags = "外部设备")
@RequestMapping("/equip_external")
public class EquipExternalController extends BaseController {

    private final EquipExternalService equipExternalService;
    @Autowired
    public EquipExternalController( EquipExternalService equipExternalService) {
        this.equipExternalService = equipExternalService;
    }




    /**
     * 外部设备借入列表
     * @param equipExternal
     * @return
     */
    @GetMapping("listPage")
    public TableDataInfo listPage(EquipExternal equipExternal){


        startPage();
        List<EquipExternal> equipExternals = equipExternalService.listPage(equipExternal);
        return getDataTable(equipExternals);
    }




    /**
     * 新增外部设备
     * @param equipExternal
     */
    @PostMapping("save")
    @Log(title = "新增外部设备", businessType = BusinessType.INSERT)
    public R save(@RequestBody EquipExternal equipExternal ){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        equipExternal.setCreateName(loginUser.getSysUser().getNickName());
        equipExternalService.save(equipExternal);
        return R.ok("新增成功");
    }


    /**
     * 修改外部设备
     * @param equipExternal
     * @return
     */
    @Log(title = "修改外部设备", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R update(@RequestBody  EquipExternal equipExternal){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        equipExternal.setUpdateBy(loginUser.getSysUser().getNickName());
        equipExternalService.updateById(equipExternal);
        return R.ok("修改外部设备成功");
    }

    /**
     * 删除外部设备
     * @param equipExternal
     * @return
     */
    @Log(title = "删除外部设备", businessType = BusinessType.DELETE)
    @PostMapping("/del")
    public R remove(@RequestBody  EquipExternal equipExternal){
        equipExternal.setDel_flag(1);
        equipExternalService.updateById(equipExternal);
        return R.ok("删除成功");
    }




}
