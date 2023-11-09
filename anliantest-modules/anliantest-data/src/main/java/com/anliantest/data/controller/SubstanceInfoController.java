package com.anliantest.data.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.data.domain.vo.SubstanceInfoVo;
import com.anliantest.data.entity.SubstanceInfoEntity;
import com.anliantest.data.service.SubstanceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/6/25 13:34
 */
@RestController
@Api("危害因素")
@RequestMapping("/substanceInfo")
public class SubstanceInfoController {

    private final SubstanceInfoService substanceInfoService;

    @Autowired
    public SubstanceInfoController(SubstanceInfoService substanceInfoService){
        this.substanceInfoService = substanceInfoService;
    }

    /**
     * 新增危害因素
     *
     */
    @PostMapping("/addSubstance")
    @ApiOperation("新增危害因素")
    public R<String> addSubstanceInfo(@RequestBody SubstanceInfoEntity substanceInfo) {

        Boolean b = substanceInfoService.addSubstanceInfo(substanceInfo);
        return b ? R.ok("新增成功") : R.fail("新增失败");
    }

    /**
     * 获取危害因素列表
     *
     */
    @GetMapping("/list")
    @ApiOperation("获取危害因素列表")
    public R getSubstanceInfoList(SubstanceInfoVo substanceInfoVo) {

        List<SubstanceInfoEntity> infoEntityList = substanceInfoService.getSubstanceInfoList(substanceInfoVo);
        return R.resultData(infoEntityList);
    }

    /**
     * 获取危害因素详情
     *
     */
    @GetMapping("/getDetail")
    @ApiOperation("获取危害因素详情")
    public R<Object> getSubstanceInfo(Long id) {

        SubstanceInfoEntity substanceInfo = substanceInfoService.getSubstanceInfo(id);
        return R.ok(substanceInfo, "查询成功");
    }


    /**
     * 修改危害因素信息
     *
     */
    @PutMapping("/updateSubstance")
    @ApiOperation("修改危害因素信息")
    public R<String> updateSubstanceInfo(@RequestBody SubstanceInfoEntity substanceInfo) {

        Boolean b = substanceInfoService.updateSubstanceInfo(substanceInfo);
        return b ? R.ok("修改成功") : R.fail("修改失败");
    }

    /**
     * 逻辑删除危害因素信息
     *
     */
    @PutMapping("/deleteSubstance")
    @ApiOperation("逻辑删除危害因素信息")
    public R<String> deleteSubstanceInfo(Long id) {

        Boolean b = substanceInfoService.deleteSubstanceInfo(id);
        return b ? R.ok("删除成功") : R.fail("删除失败");
    }

}
