package com.anliantest.data.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.exception.UtilException;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.data.entity.ProjectType;
import com.anliantest.data.service.ProjectTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  项目类型管理表
 * @author zhanghao
 * @date 2023-07-17
 */
@RestController
@Api(tags = "项目类型管理表")
@RequestMapping("/project_type")
public class ProjectTypeController extends BaseController {


    private final ProjectTypeService projectTypeService;
    @Autowired
    public ProjectTypeController(ProjectTypeService projectTypeService) {
        this.projectTypeService = projectTypeService;
    }


    /**
     * 项目类型管理表分页
     * @param projectType
     * @return
     */
    @GetMapping("listPage")
    public TableDataInfo listPage(ProjectType projectType){
        startPage();
        List<ProjectType> projectTypes = projectTypeService.projectTypeList(projectType);
        return getDataTable(projectTypes);
    }




    /**
     * 新增项目类型管理表
     * @param projectType
     * @return
     */
    @PostMapping("save")
    @Log(title = "新增项目类型管理表", businessType = BusinessType.INSERT)
    public R save(@RequestBody ProjectType projectType ){
         verify(projectType);
         projectTypeService.save(projectType);
        return R.ok("新增成功");
    }


    /**
     * 修改项目类型管理表
     * @param projectType
     * @return
     */
    @Log(title = "修改项目类型管理表", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public R update(@RequestBody  ProjectType projectType){
        verify(projectType);
        projectTypeService.updateById(projectType);
        return R.ok("修改成功");
    }

   /**
     * 删除项目类型管理数据
     */
    @DeleteMapping("/del")
    @Log(title = "删除项目类型管理数据", businessType = BusinessType.DELETE)
    public R remove(@RequestBody ProjectType projectType) {
        // TODO: 2023-07-20 验证该类型是否被绑定
        projectTypeService.removeById(projectType.getId());
        return R.ok("删除成功");
    }


    //重复验证
    public void verify(ProjectType projectType) {
        LambdaQueryWrapper<ProjectType> wrapper = new LambdaQueryWrapper<ProjectType>()
                .eq(ProjectType::getCompanyId, projectType.getCompanyId())
                .eq(ProjectType::getName , projectType.getName())
                .ne(projectType.getId() != null, ProjectType::getId, projectType.getId());



        long count = projectTypeService.count(wrapper);

        if (count > 0) {
            throw new UtilException("当前公司该类型名称已存在");
        }

        LambdaQueryWrapper<ProjectType> wrapper2 = new LambdaQueryWrapper<ProjectType>()
                .eq(ProjectType::getCompanyId, projectType.getCompanyId())
                .eq(ProjectType::getCode, projectType.getCode())
                .ne(projectType.getId() != null, ProjectType::getId, projectType.getId());
        long count2 = projectTypeService.count(wrapper2);
        if (count2 > 0) {
            throw new UtilException("当前公司该类型编号已存在");
        }



    }



}
