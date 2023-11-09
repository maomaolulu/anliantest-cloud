package com.anliantest.data.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.exception.UtilException;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.data.entity.ContractType;
import com.anliantest.data.service.ContractTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合同类型
 * @author zhanghao
 * @date 2023-07-28
 * @desc : 合同类型
 */
@RestController
@Api(tags = "合同类型")
@RequestMapping("/contract_type")
public class ContractTypeController extends BaseController {


    private ContractTypeService contractTypeService;
    @Autowired
    public ContractTypeController(ContractTypeService contractTypeService) {
        this.contractTypeService = contractTypeService;
    }


    /**
     * 合同类别分页
     * @param contractType
     * @return
     */
    @GetMapping("typeListPage")
    public TableDataInfo typeListPage(ContractType contractType){
        startPage();
        List<ContractType> contractTypes = contractTypeService.typeListPage(contractType);
        return getDataTable(contractTypes);
    }



    /**
     * 事业部下:一级分类
     * @param businessUnitId
     * @return
     */
    @GetMapping("typeList")
    public R typeList(Long businessUnitId){

        List<ContractType> contractTypes = contractTypeService.list(new QueryWrapper<ContractType>()
                .eq("business_unit_id",businessUnitId)
                .eq("pid",0L)
        );
        return R.ok(contractTypes);
    }



    /**
     * 新增合同类型
     * @param contractType
     * @return
     */
    @PostMapping("save")
    @Log(title = "新增合同类型", businessType = BusinessType.INSERT)
    public R save(@RequestBody ContractType contractType ){
        if(contractType.getPid()==0){
            verifyOne(contractType);
        }else {
            verifyTwo(contractType);
        }
         contractTypeService.save(contractType);
        return R.ok("新增成功");
    }


    /**
     * 修改合同类型
     * @param contractType
     * @return
     */
    @Log(title = "修改合同类型", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public R update(@RequestBody  ContractType contractType){
        if(contractType.getPid()==0){
            verifyOne(contractType);
        }else {
            verifyTwo(contractType);
        }
        contractTypeService.updateById(contractType);
        return R.ok("修改成功");
    }

   /**
     * 删除合同类型
     */
    @DeleteMapping("/del")
    @Log(title = "删除合同类型", businessType = BusinessType.DELETE)
    public R remove(@RequestBody ContractType contractType) {

        contractTypeService.removeById(contractType.getId());
        return R.ok("删除成功");
    }



    //重复验证
    public void verifyTwo(ContractType BusinessType) {
        LambdaQueryWrapper<ContractType> wrapper = new LambdaQueryWrapper<ContractType>()
                .eq(ContractType::getCompanyId, BusinessType.getCompanyId())
                .eq(ContractType::getName , BusinessType.getName())
                .eq(ContractType::getBusinessUnitId , BusinessType.getBusinessUnitId())
                .ne(ContractType::getPid , 0L)
                .ne(BusinessType.getId() != null, ContractType::getId, BusinessType.getId());



        long count = contractTypeService.count(wrapper);

        if (count > 0) {
            throw new UtilException("当前事业部该公司该二级类型名称已存在");
        }

    }
    //重复验证
    public void verifyOne(ContractType BusinessType) {
        LambdaQueryWrapper<ContractType> wrapper = new LambdaQueryWrapper<ContractType>()
                .eq(ContractType::getBusinessUnitId, BusinessType.getBusinessUnitId())
                .eq(ContractType::getName , BusinessType.getName())
                .eq(ContractType::getPid , 0L)
                .ne(BusinessType.getId() != null, ContractType::getId, BusinessType.getId());

        long count = contractTypeService.count(wrapper);

        if (count > 0) {
            throw new UtilException("当前事业部该一级类型名称已存在");
        }

    }

}
