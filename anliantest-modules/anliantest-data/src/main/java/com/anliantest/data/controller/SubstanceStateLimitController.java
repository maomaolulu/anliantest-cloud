package com.anliantest.data.controller;


import com.anliantest.common.core.domain.R;
import com.anliantest.data.entity.SubstanceStateLimitEntity;
import com.anliantest.data.service.SubstanceStateLimitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/6/06 19:00
 */
@RestController
@Api("物质不同条件限值表")
@RequestMapping("/substancestatelimit")
public class SubstanceStateLimitController {

    private final SubstanceStateLimitService substanceStateLimitService;

    @Autowired
    public SubstanceStateLimitController(SubstanceStateLimitService substanceStateLimitService){
        this.substanceStateLimitService = substanceStateLimitService;
    }

    /**
     * 获取物质不同条件限值列表
     *
     */
    @GetMapping("/list")
    @ApiOperation("获取检测项目列表")
    public R<Object> list() {
        List<SubstanceStateLimitEntity> stateLimits = substanceStateLimitService.list();
        return R.ok(stateLimits, "查询成功");
    }


    /**
     * 新增危害因素限值
     *
     */
    @PostMapping("/addLimit")
    @ApiOperation("新增危害因素限值")
    public R<Object> addLimit(@RequestBody SubstanceStateLimitEntity stateLimit) {

        Boolean b = substanceStateLimitService.addLimit(stateLimit);
        return b ? R.ok(stateLimit, "新增成功") : R.fail(null, "新增失败");
    }

    /**
     * 获取危害因素限值
     *
     */
    @GetMapping("/getBySubstanceId")
    @ApiOperation("获取危害因素限值")
    public R<Object> getListBySubstanceInfoId(Long substanceInfoId) {
        List<SubstanceStateLimitEntity> stateLimitEntityList = substanceStateLimitService.getListBySubstanceInfoId(substanceInfoId);
        return R.ok(stateLimitEntityList, "查询成功");
    }


    /**
     * 修改危害因素限值
     *
     */
    @PutMapping("/updateLimit")
    @ApiOperation("修改危害因素限值")
    public R<Object> updateLimit(@RequestBody SubstanceStateLimitEntity stateLimit) {

        Boolean b = substanceStateLimitService.updateLimit(stateLimit);
        return b ? R.ok(stateLimit, "修改成功") : R.fail(null, "修改失败");
    }

    /**
     * 逻辑删除危害因素限值
     *
     */
    @PutMapping("/deleteLimit")
    @ApiOperation("逻辑删除危害因素限值")
    public R<String> deleteLimit(Long id) {
        Boolean b = substanceStateLimitService.deleteLimit(id);
        return b ? R.ok("删除成功") : R.fail("删除失败");
    }


}
