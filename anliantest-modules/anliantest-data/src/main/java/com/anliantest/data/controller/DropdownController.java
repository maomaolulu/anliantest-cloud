package com.anliantest.data.controller;

import cn.hutool.core.util.StrUtil;
import com.anliantest.common.core.domain.R;
import com.anliantest.data.entity.ContractType;
import com.anliantest.data.entity.SubstanceInfoEntity;
import com.anliantest.data.entity.SubstanceTestLawEntity;
import com.anliantest.data.service.ContractTypeService;
import com.anliantest.data.service.SubstanceInfoService;
import com.anliantest.data.service.SubstanceTestLawService;
import com.anliantest.message.api.RemoteMailMessageSendService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * data下拉框
 *
 * @author zh
 * @date 2023-06-28
 */
@RestController
@Api(tags = "下拉框")
@RequestMapping("/data/dropdown")
public class DropdownController {


    private final SubstanceInfoService substanceInfoService;
    private final SubstanceTestLawService substanceTestLawService;
    private final RemoteMailMessageSendService remoteMailMessageSendService;
    private final ContractTypeService contractTypeService;

    @Autowired
    public DropdownController(SubstanceInfoService substanceInfoService, SubstanceTestLawService substanceTestLawService, RemoteMailMessageSendService remoteMailMessageSendService, ContractTypeService contractTypeService) {
        this.substanceInfoService = substanceInfoService;
        this.substanceTestLawService = substanceTestLawService;
        this.remoteMailMessageSendService = remoteMailMessageSendService;
        this.contractTypeService = contractTypeService;
    }

    /**
     * 物质检测方法：危害因素下拉
     */
    @GetMapping("/substanceInfoDropdownList")
    public List<SubstanceInfoEntity> substanceInfoDropdownList(String substanceName) {
        return substanceInfoService.list(new QueryWrapper<SubstanceInfoEntity>()
                .like(StrUtil.isNotBlank(substanceName), "substance_name", substanceName)
                .eq("delete_flag",0)
                .last("limit 30 "));
    }



    /**
     * 物质检测方法:检测对象、标准号、标准名下拉框
     *
     * @return R
     */
    @GetMapping("/dropdownList")
    @ApiOperation("物质检测方法:检测对象、标准号、标准名下拉框")
    public R dropdownList(SubstanceTestLawEntity substanceTestLaw) {

        List<SubstanceTestLawEntity> list = substanceTestLawService.list(new QueryWrapper<SubstanceTestLawEntity>()
                .like(StrUtil.isNotBlank(substanceTestLaw.getTestStandards()), "test_standards", substanceTestLaw.getTestStandards())
                .like(StrUtil.isNotBlank(substanceTestLaw.getTestStandardsName()), "test_standards_name", substanceTestLaw.getTestStandardsName())
                .eq(substanceTestLaw.getTestCategory() != null, "test_category", substanceTestLaw.getTestCategory())
                .isNotNull("test_category")
                .eq("delete_flag",0)
                .last("limit 30")
        );
        return R.ok(list);
    }

    /**
     * 二级合同类型下拉框（合同范本选择）
     */
    @GetMapping("/dropDownContractType")
    public R dropDownContractType(String name) {

        List<ContractType> contractTypes = contractTypeService.dropDownContractType(name);

        return R.ok(contractTypes);
    }
}
