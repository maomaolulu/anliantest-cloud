package com.anliantest.system.controller;

import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.domain.AjaxResult;
import com.anliantest.system.api.domain.SysDept;
import com.anliantest.system.api.domain.SysRole;
import com.anliantest.system.constants.SysConstant;
import com.anliantest.system.service.ISysDeptService;
import com.anliantest.system.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Sys下拉框
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/dropdown")
public class SysDropdownController extends BaseController {

    private final ISysDeptService deptService;

    private final ISysRoleService sysRoleService;
    @Autowired
    public SysDropdownController(ISysDeptService deptService, ISysRoleService sysRoleService) {
        this.deptService = deptService;
        this.sysRoleService = sysRoleService;
    }

    /**
     * 获取公司下拉框
     */
    @GetMapping("/companyList")
    public AjaxResult companyList(String deptName) {
        List<SysDept> list = deptService.list(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getDeptId, SysDept::getDeptName)
                .eq(SysDept::getParentId, SysConstant.COMPANY_PARENT_ID)
                .eq(SysDept::getDelFlag, 0)
                .like(StringUtils.isNotEmpty(deptName),SysDept::getDeptName,deptName)
        );
        return success(list);
    }
    /**
     * 获取事业部下拉框
     */
    @GetMapping("/businessUnitList")
    public AjaxResult businessUnitList(String deptName) {
        List<SysDept> list = deptService.list(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getDeptId, SysDept::getDeptName)
                .eq(SysDept::getParentId, SysConstant.BUSINESS_UNIT_PARENT_ID)
                .eq(SysDept::getDelFlag, 0)
                .like(StringUtils.isNotEmpty(deptName),SysDept::getDeptName,deptName)
        );
        return success(list);
    }
    /**
     * 角色下拉框
     *
     * @param roleName 角色名称
     * @return 结果
     */
    @GetMapping("/sysRoleListByRoleName")
    public AjaxResult sysRoleListByRoleName(String roleName) {
        List<SysRole> sysRoles = sysRoleService.sysRoleListByRoleName(roleName);
        return success(sysRoles);
    }



}
