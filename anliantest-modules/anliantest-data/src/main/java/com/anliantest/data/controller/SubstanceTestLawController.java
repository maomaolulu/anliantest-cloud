package com.anliantest.data.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.enums.Numbers;
import com.anliantest.common.core.utils.PageUtils;
import com.anliantest.common.core.utils.pageUtil;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.security.annotation.InnerAuth;
import com.anliantest.data.domain.dto.SubstanceTestLawDto;
import com.anliantest.data.entity.SubstanceTestLawEntity;
import com.anliantest.data.service.SubstanceTestLawService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author gy
 * @version 1.0
 * @date 2023-06-06 19:06
 */
@RestController
@Api("物质检测标准法律法规表")
@RequestMapping("/substancetestlaw")
public class SubstanceTestLawController extends BaseController {

    @Autowired
    private SubstanceTestLawService substanceTestLawService;
    private final String moreBlank = "  ";
    private final String testStandard = "test_standards";

    /**
     * 获取检测标准/法律法规列表
     * @return R
     */
    @GetMapping("/list")
    @ApiOperation("获取物质检测标准/法律法规表列表")
    public TableDataInfo list(SubstanceTestLawDto substanceTestLawdto) {
        startPage();
        return getDataTable(substanceTestLawService.listByCondition(substanceTestLawdto));
    }

    /**
     * 新增检测标准/法律法规
     * @return R
     */
    @PostMapping("/insert")
    @ApiOperation("新增物质检测标准/法律法规表")
    public R<String> insert(@RequestBody @Validated SubstanceTestLawDto substanceTestLawdto,BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            if (substanceTestLawdto.getTestStandardsName().contains(moreBlank)) {
                return R.fail("名称包含多个空格,请检查");
            }
            if (substanceTestLawService.list(new QueryWrapper<SubstanceTestLawEntity>().eq(testStandard, substanceTestLawdto.getTestStandards())).size() > 0) {
                return R.fail("该文号已存在,请检查");
            }
            return substanceTestLawService.saveByCondition(substanceTestLawdto) ? R.ok("新增成功") : R.fail("新增失败");
        } else {
            return R.fail(Objects.requireNonNull(bindingResult.getFieldError()).getField() + bindingResult.getFieldError().getDefaultMessage());
        }
    }

    /**
     * 修改检测标准/法律法规
     * @return R
     */
    @PutMapping("/update")
    @ApiOperation("修改物质检测标准/法律法规表")
    public R<String> update(@RequestBody @Validated SubstanceTestLawDto substanceTestLawdto, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            if (substanceTestLawdto.getTestStandardsName().contains(moreBlank)) {
                return R.fail("名称包含多个空格,请检查");
            }
            return substanceTestLawService.updateByCondition(substanceTestLawdto) ? R.ok("修改成功") : R.fail("修改失败");
        } else {
            return R.fail(Objects.requireNonNull(bindingResult.getFieldError()).getField() + bindingResult.getFieldError().getDefaultMessage());
        }
    }

    /**
     * 删除检测标准/法律法规
     * @return R
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除物质检测标准/法律法规表")
    public R<String> delete(Long id) {
        return substanceTestLawService.deleteByCondition(id) ? R.ok("删除成功") : R.fail("删除失败");
    }

    /**
     * 刷新状态
     */
    @PostMapping("/refreshStatus")
    @ApiOperation("刷新状态")
    public void refreshStatus(){
        substanceTestLawService.refreshStatus();
    }
}
