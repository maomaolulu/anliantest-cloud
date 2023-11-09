package com.anliantest.project.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.enums.DeleteFlag;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.project.entity.ContractField;
import com.anliantest.project.service.ContractFieldService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 合同字段库
 * @author ZhuYiCheng
 * @date 2023/10/16 15:13
 */
@RestController
@Api("合同字段库")
@RequestMapping("/contractField")
public class ContractFieldController extends BaseController {

    private final ContractFieldService contractFieldService;

    @Autowired
    public ContractFieldController(ContractFieldService contractFieldService){
        this.contractFieldService = contractFieldService;
    }

    /**
     * 不分页供新建条款时使用的字段列表查询
     */
    @GetMapping("/getList")
    public R<Object> getList() {

        List<ContractField> list = contractFieldService.list(new QueryWrapper<ContractField>().eq("del_flag", DeleteFlag.NO.ordinal()));
        return R.ok(list);
    }

}
