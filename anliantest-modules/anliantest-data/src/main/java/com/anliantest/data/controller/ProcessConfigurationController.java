package com.anliantest.data.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.data.domain.dto.ProcessConfigurationDto;
import com.anliantest.data.entity.ProcessConfiguration;
import com.anliantest.data.service.ProcessConfigurationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程配置表
 * @author zhanghao
 * @date 2023-07-17
 */
@RestController
@Api(tags = "流程配置表")
@RequestMapping("/process_configuration")
public class ProcessConfigurationController extends BaseController {


    private ProcessConfigurationService processConfigurationService;
    @Autowired
    public ProcessConfigurationController(ProcessConfigurationService processConfigurationService) {
        this.processConfigurationService = processConfigurationService;
    }


    /**
     * 流程配置表分页
     * @param processConfigurationDto
     * @return
     */
    @GetMapping("listPage")
    public TableDataInfo listPage(ProcessConfigurationDto processConfigurationDto){
        startPage();
        List<ProcessConfigurationDto> processConfigurationDtos = processConfigurationService.processConfigurationList(processConfigurationDto);
        return getDataTable(processConfigurationDtos);
    }



    /**
     * 编辑流程配置表
     * @param processConfiguration
     * @return
     */
    @Log(title = "编辑流程配置表", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public R update(@RequestBody  ProcessConfiguration processConfiguration){

        processConfigurationService.updateById(processConfiguration);
        return R.ok("编辑成功");
    }


    /**
     * 新增流程配置表
     * @param processConfiguration
     * @return
     */
    @PostMapping("save")
    @Log(title = "新增流程配置表", businessType = BusinessType.INSERT)
    public R save(@RequestBody ProcessConfiguration processConfiguration ){

         processConfigurationService.save(processConfiguration);
        return R.ok("新增成功");
    }




//   /**
//     * 删除流程配置表
//     */
//    @DeleteMapping("/del")
//    @OperateLog(title = "删除流程配置表")
//    public R remove(@RequestBody List<Long> roleIds) {
//
//        processConfigurationService.removeByIds(roleIds);
//        return R.ok("删除成功");
//    }


//    //重复验证
//    public void verify(ProcessConfiguration processConfiguration) {
//
//        long count = processConfigurationService.count(new LambdaQueryWrapper<ProcessConfiguration>()
//                .eq(ProcessConfiguration::ge, processConfiguration.getTestName())
//                .eq(ProcessConfiguration::getTestNumber, processConfiguration.getTestNumber())
//                .ne(processConfiguration.getId() != null, ProcessConfiguration::getId, processConfiguration.getId())
//        );
//        if (count > 0) {
//            throw new UtilException("方法名和方法号已存在");
//        }
//
//    }

}
