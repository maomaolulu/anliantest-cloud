package com.anliantest.project.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.project.entity.ContractTermType;
import com.anliantest.project.service.ContractTermTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 合同条款类型
 *
 * @author ZhuYiCheng
 * @date 2023/10/16 9:31
 */
@RestController
@Api("合同条款类型")
@RequestMapping("/contractTermType")
public class ContractTermTypeController extends BaseController {

    private final ContractTermTypeService contractTermTypeService;

    @Autowired
    public ContractTermTypeController(ContractTermTypeService contractTermTypeService){
        this.contractTermTypeService = contractTermTypeService;
    }

    /**
     * 新增合同条款类型
     */
    @Log(title = "新增合同条款类型", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    public R<String> insert(@RequestBody ContractTermType contractTermType) {

        //校验唯一性，所属同公司的条款类型名称唯一
        if (!contractTermTypeService.checkName(contractTermType)) {
            return R.fail("该类型已存在");
        }
        //新增数据
        return contractTermTypeService.add(contractTermType) ? R.ok(null, "新增成功") : R.fail("新增失败");
    }


    /**
     * 合同条款类型列表查询
     */
    @GetMapping("/list")
    public R<Object> list() {

        Map<Long, List<ContractTermType>> listMap = contractTermTypeService.getList();
        return R.ok(listMap);
    }


    /**
     * 条款类型详情
     */
    @GetMapping("/info")
    public R<Object> info(Long id) {

        return R.ok(contractTermTypeService.getById(id));
    }


    /**
     * 编辑合同条款类型
     */
    @Log(title = "编辑合同条款类型", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public R<String> update(@RequestBody ContractTermType contractTermType) {

        String newName = contractTermType.getName();
        ContractTermType oldTermType = contractTermTypeService.getById(contractTermType.getId());
        //比较新老类型名称是否相同 && 校验唯一性，所属同公司的条款类型名称唯一
        if (!newName.equals(oldTermType.getName()) && !contractTermTypeService.checkName(contractTermType)) {
            return R.fail("该类型已存在");
        }

        return contractTermTypeService.updateTermType(contractTermType) ? R.ok(null, "编辑成功") : R.fail("编辑失败");
    }


    /**
     * 校验是否存在关联条款
     */
    @GetMapping("/check")
    public R<String> check(Long id) {

        return contractTermTypeService.check(id) ? R.ok() : R.fail("该类型已创建条款，请清空后删除！");
    }


    /**
     * 删除合同条款类型
     */
    @Log(title = "删除合同条款类型", businessType = BusinessType.DELETE)
    @PutMapping("/remove")
    public R<String> remove(Long id) {

        return contractTermTypeService.removeTermType(id) ? R.ok(null, "删除成功") : R.fail("删除失败");
    }

    /**
     * 根据公司id查询条款类型
     */
    @GetMapping("/selectByCompanyId")
    public TableDataInfo selectByCompanyId(Long companyId) {
        List<ContractTermType> list = contractTermTypeService.list(new LambdaQueryWrapper<ContractTermType>()
                .eq(ContractTermType::getCompanyId, companyId)
                .eq(ContractTermType::getDelFlag, 0)
                .orderByAsc(ContractTermType::getOrderNum)
        );
        return getDataTable(list);
    }
}
