package com.anliantest.system.controller;

import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson2.JSONArray;
import com.anliantest.common.core.constant.CacheConstants;
import com.anliantest.common.core.constant.DeptConstants;
import com.anliantest.common.core.exception.UtilException;
import com.anliantest.common.core.exception.auth.NotLoginException;
import com.anliantest.common.core.utils.SpringUtils;
import com.anliantest.common.redis.service.RedisService;
import com.anliantest.system.api.domain.SysDictData;
import com.anliantest.system.service.ISysDictDataService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.anliantest.common.core.constant.UserConstants;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.domain.AjaxResult;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.common.security.annotation.RequiresPermissions;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.system.api.domain.SysDept;
import com.anliantest.system.service.ISysDeptService;

/**
 * 部门信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 获取部门列表
     */
    @RequiresPermissions("system:dept:list")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @RequiresPermissions("system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        depts.removeIf(d -> d.getDeptId().intValue() == deptId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""));
        return success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @RequiresPermissions("system:dept:query")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId)
    {
        deptService.checkDeptDataScope(deptId);
        return success(deptService.selectDeptById(deptId));
    }

    /**
     * 根据部门id获取详细信息
     */
    @GetMapping(value = "/info/{deptId}")
    public SysDept getInfoByDeptId(@PathVariable Long deptId)
    {
        if(deptId==null){
            throw new UtilException("部门id不能为空");
        }
        SysDept byId = deptService.getById(deptId);


        return byId;
    }

    /**
     * 新增部门
     */
    @RequiresPermissions("system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept)
    {
        if (!deptService.checkDeptNameUnique(dept))
        {
            return error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        Long parentId = dept.getParentId();
        if(dept != null && parentId != null){
            if(!parentId.equals(DeptConstants.DEPT_ANLIANTEST_ID)){
                String companyKey = "";
                if(parentId.equals(DeptConstants.DEPT_EHS_ID)){
                    //维护字典
                    companyKey = dictDataService.saveEhsDictData(dept.getDeptName());
                }else{
                    SysDept sysDept = deptService.selectDeptById(parentId);
                    if(sysDept != null){
                        companyKey = sysDept.getCompanyKey();
                    }
                }
                if(StringUtils.isBlank(companyKey)){
                    return error("新增部门'" + dept.getDeptName() + "'失败，companyKey获取失败");
                }
                dept.setCompanyKey(companyKey);
            }else {
                return error("新增事业部级别部门请联系系统管理员");
            }
        }
        int i = deptService.insertDept(dept);
        SpringUtils.getBean(RedisService.class).deleteObject(CacheConstants.SYS_DEPT_KEY);
        return toAjax(i);
    }

    /**
     * 修改部门
     */
    @RequiresPermissions("system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept)
    {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (!deptService.checkDeptNameUnique(dept))
        {
            return error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        else if (dept.getParentId().equals(deptId))
        {
            return error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0)
        {
            return error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        int i = deptService.updateDept(dept);
        SpringUtils.getBean(RedisService.class).deleteObject(CacheConstants.SYS_DEPT_KEY);
        return toAjax(i);
    }

    /**
     * 删除部门
     */
    @RequiresPermissions("system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return warn("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return warn("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        int i = deptService.deleteDeptById(deptId);
        SpringUtils.getBean(RedisService.class).deleteObject(CacheConstants.SYS_DEPT_KEY);
        return toAjax(i);
    }

    /**
     * 查询部门列表(用于字段映射)
     */
    @GetMapping("/deptList")
    public List<SysDept> deptList()
    {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        JSONArray arrayCache = redisService.getCacheObject(CacheConstants.SYS_DEPT_KEY);
        if (StringUtils.isNotNull(arrayCache))
        {
            return arrayCache.toList(SysDept.class);
        }


        List<SysDept> list = deptService.list(new QueryWrapper<SysDept>().select("dept_id","dept_name","parent_id","ancestors"));
        redisService.setCacheObject(CacheConstants.SYS_DEPT_KEY, list);
        return list;
    }

}
