package com.anliantest.project.controller;

import cn.hutool.core.collection.CollUtil;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.project.entity.ContractSample;
import com.anliantest.project.entity.ContractSampleTerm;
import com.anliantest.project.service.ContractSampleService;
import com.anliantest.project.service.ContractSampleTermService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 合同范本
 * @author zh
 * @date 2023-10-16
 */
@RestController
@RequestMapping("/contract_sample")
public class ContractSampleController extends BaseController {


    private final ContractSampleService contractSampleService;
    private final ContractSampleTermService contractSampleTermService;

    @Autowired
    public ContractSampleController(ContractSampleService contractSampleService, ContractSampleTermService contractSampleTermService) {
        this.contractSampleService = contractSampleService;
        this.contractSampleTermService = contractSampleTermService;
    }

    /**
     * 合同范本分页
     * @param contractSample
     * @return
     */
    @GetMapping("listPage")
    public TableDataInfo listPage(ContractSample contractSample){
        startPage();
        List<ContractSample> contractSamples = contractSampleService.listPage(contractSample);
        return getDataTable(contractSamples);
    }


    /**
     * 预览合同范本
     * @param id
     * @return
     */
    @GetMapping("previewList")
    public TableDataInfo previewList(Long id){
        startPage();
        List<ContractSampleTerm> list = contractSampleService.previewList(id);
        return getDataTable(list);
    }


    /**
     * 新增合同范本
     * @param contractSample
     * @return result
     */
    @PostMapping("/save")
    @Log(title = "新增合同范本", businessType = BusinessType.INSERT)
    public R save(@RequestBody ContractSample contractSample) {
        if(CollUtil.isEmpty(contractSample.getContractSampleTerms())){
            return R.fail("至少选择一个条款");
        }
        contractSample.setCreateBy(SecurityUtils.getUsernameCn());
        contractSample.setCreateById(SecurityUtils.getUserId());
        contractSample.setCreateTime(new Date());
        contractSampleService.saveContractSample(contractSample);

        return R.ok(contractSample);
    }


    /**
     * 停用合同范本
     * @param contractSample
     * @return result
     */
    @PostMapping("/deactivate")
    @Log(title = "停用合同范本", businessType = BusinessType.UPDATE)
    public R deactivate(@RequestBody ContractSample contractSample) {


        contractSample.setStatus(0);
        contractSampleService.updateById(contractSample);

        return R.ok(contractSample);
    }
    /**
     * 启用合同范本
     * @param contractSample
     * @return result
     */
    @PostMapping("/startUsing")
    @Log(title = "启用合同范本", businessType = BusinessType.UPDATE)
    public R startUsing(@RequestBody ContractSample contractSample) {


        contractSample.setStatus(1);
        contractSampleService.updateById(contractSample);

        return R.ok(contractSample);
    }

    /**
     * 删除合同范本
     * @param contractSample
     * @return result
     */
    @PostMapping("/remove")
    @Log(title = "删除合同范本", businessType = BusinessType.DELETE)
    public R remove(@RequestBody ContractSample contractSample) {


        // TODO: 2023-10-16 验证是否有合同绑定范本
        //范本不做逻辑删
        contractSampleService.removeContractSample(contractSample);

        return R.ok(contractSample);
    }



}
