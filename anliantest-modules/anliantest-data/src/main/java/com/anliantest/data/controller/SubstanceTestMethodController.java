package com.anliantest.data.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.exception.UtilException;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.data.domain.dto.SubstanceTestMethodDto;
import com.anliantest.data.entity.SubstanceTestMethodEntity;
import com.anliantest.data.service.SubstanceTestMethodService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gy
 * @Description 物质检测方法表
 * @date 2023-06-06 19:08
 */
@RestController
@Api("物质检测方法表")
@RequestMapping("/substancetestmethod")
public class SubstanceTestMethodController extends BaseController {

    @Autowired
    private SubstanceTestMethodService substanceTestMethodService;

    /**
     * 获取检测方法分页列表
     *
     * @return
     */
    @GetMapping("/listPage")
    @ApiOperation("获取检测方法分页列表")
    public TableDataInfo list(SubstanceTestMethodDto substanceTestMethodDto) {
        startPage();
        List<SubstanceTestMethodDto> list = substanceTestMethodService.pageList(substanceTestMethodDto);
        return getDataTable(list);
    }

    /**
     * 获取检测方法详情
     *
     * @return
     */
    @GetMapping("/info")
    @ApiOperation("获取检测方法详情")
    public R info(Long id) {
        return R.ok(substanceTestMethodService.info(id));
    }

    /**
     * 新增检测方法
     *
     * @param list
     * @return
     */
    @PostMapping("save")
//    @OperateLog(title = "新增检测方法")
    @Log(title = "新增检测方法", businessType = BusinessType.INSERT)
    public R save(@RequestBody List<SubstanceTestMethodEntity> list) {

        for (SubstanceTestMethodEntity substanceTestMethodEntity : list
        ) {
            verify(substanceTestMethodEntity);
        }

        substanceTestMethodService.saveBatch(list);
        return R.ok("新增成功");
    }


    /**
     * 修改检测方法
     *
     * @param substanceTestMethodEntity
     * @return
     */
    @Log(title = "修改检测方法", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public R update(@RequestBody SubstanceTestMethodEntity substanceTestMethodEntity) {
        verify(substanceTestMethodEntity);

        substanceTestMethodService.updateById(substanceTestMethodEntity);
        return R.ok("修改成功");
    }

    /**
     * 删除检测方法
     */
    @DeleteMapping("/del")
    @Log(title = "删除检测方法", businessType = BusinessType.DELETE)
    public R remove(@RequestBody SubstanceTestMethodEntity substanceTestMethodEntity) {

        // TODO: 2023-06-26 判断有无资质
        substanceTestMethodService.removeById(substanceTestMethodEntity.getId());

        return R.ok("删除成功");
    }

    //重复验证
    public void verify(SubstanceTestMethodEntity substanceTestMethodEntity) {

        long count = substanceTestMethodService.count(new LambdaQueryWrapper<SubstanceTestMethodEntity>()
                .eq(SubstanceTestMethodEntity::getTestName, substanceTestMethodEntity.getTestName())
                .eq(SubstanceTestMethodEntity::getTestNumber, substanceTestMethodEntity.getTestNumber())
                .ne(substanceTestMethodEntity.getId() != null, SubstanceTestMethodEntity::getId, substanceTestMethodEntity.getId())
        );
        if (count > 0) {
            throw new UtilException("方法名和方法号已存在");
        }

    }


}
