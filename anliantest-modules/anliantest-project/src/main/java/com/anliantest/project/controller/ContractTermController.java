package com.anliantest.project.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.project.domain.dto.ContractTermDto;
import com.anliantest.project.entity.ContractTerm;
import com.anliantest.project.service.ContractTermService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合同条款
 *
 * @author ZhuYiCheng
 * @date 2023/10/16 15:43
 */
@RestController
@Api("合同条款")
@RequestMapping("/contractTerm")
public class ContractTermController extends BaseController {

    private final ContractTermService contractTermService;

    @Autowired
    public ContractTermController(ContractTermService contractTermService){
        this.contractTermService = contractTermService;
    }

    /**
     * 新增条款
     */
    @Log(title = "新增条款", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    public R<String> insert(@RequestBody ContractTermDto contractTermDto) {

        return contractTermService.add(contractTermDto) ? R.ok(null, "新增成功") : R.fail("新增失败,请联系管理员");
    }


    /**
     * 条款列表查询
     */
    @GetMapping("/list")
    public TableDataInfo list(ContractTermDto contractTermDto) {
        startPage();
        List<ContractTerm> list = contractTermService.getList(contractTermDto);
        return getDataTable(list);
    }

    /**
     * 根据条款类型查询条款分页
     */
    @GetMapping("/listByTermTypeId")
    public TableDataInfo listByTermTypeId(ContractTerm contractTerm) {
        startPage();
        List<ContractTerm> list = contractTermService.listByTermTypeId(contractTerm);

        return getDataTable(list);
    }


    /**
     * 条款详情查询
     */
    @GetMapping("/info")
    public R<Object> info(Long id) {

        return R.ok(contractTermService.getInfo(id));
    }


    /**
     * 编辑条款
     */
    @Log(title = "编辑条款", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public R<String> update(@RequestBody ContractTermDto contractTermDto) {

        return contractTermService.updateTerm(contractTermDto) ? R.ok(null, "编辑成功") : R.fail("编辑失败,请联系管理员");
    }


    /**
     * 编辑条款状态，停用/启用
     */
    @Log(title = "编辑条款状态，停用/启用", businessType = BusinessType.UPDATE)
    @PutMapping("/updateStatus")
    public R<String> updateStatus(@RequestBody ContractTermDto contractTermDto) {

        return contractTermService.updateStatus(contractTermDto) ? R.ok(null, "操作成功") : R.fail("操作失败,请联系管理员");
    }


    /**
     * 校验是否存在关联合同范本
     */
    @GetMapping("/check")
    public R<String> check(Long id) {

        return contractTermService.check(id) ? R.ok() : R.fail("该条款已被使用，不可删除！");
    }


    /**
     * 删除条款
     */
    @Log(title = "删除条款", businessType = BusinessType.DELETE)
    @PutMapping("/remove")
    public R<String> remove(Long id) {

        return contractTermService.removeTerm(id) ? R.ok(null, "删除成功") : R.fail("删除失败");
    }

}
