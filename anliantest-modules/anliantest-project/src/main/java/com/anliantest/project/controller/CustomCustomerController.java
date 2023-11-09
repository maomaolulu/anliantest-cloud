package com.anliantest.project.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.project.domain.dto.CustomCustomerDto;
import com.anliantest.project.domain.dto.RoleAndCompanyDto;
import com.anliantest.project.domain.vo.CustomCustomerVo;
import com.anliantest.project.entity.CustomAdvanceTaskEntity;
import com.anliantest.project.entity.CustomCustomerEntity;
import com.anliantest.project.service.CustomAdvanceTaskService;
import com.anliantest.project.service.CustomCustomerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户模块
 * @author gy
 * @date 2023-08-18 17:02
 */
@RestController
@Api(tags = "客户")
@RequestMapping("/customCustomer")
public class CustomCustomerController extends BaseController {

    @Autowired
    private CustomCustomerService customCustomerService;
    @Autowired
    private CustomAdvanceTaskService customAdvanceTaskService;

    /**
     * 客户列表分页
     * @param dto
     * @return TableDataInfo
     */
    @GetMapping("/listWithPage")
    public TableDataInfo listWithPage(CustomCustomerDto dto){
        startPage();
        List<CustomCustomerVo> list = customCustomerService.listWithPage(dto);
        return getDataTable(list);
    }

    /**
     * 客户以及联系人新增
     * @param dto
     * @return R
     */
    @PutMapping("/newCustomer")
    public R<String> newCustomer(@RequestBody CustomCustomerDto dto){
        List<RoleAndCompanyDto> roleAndCompany = customCustomerService.getRoleAndCompany(SecurityUtils.getUserId());
        if (roleAndCompany.size() > 0){
            String companyNow = roleAndCompany.get(0).getDictLabel();
            dto.setCustomerCompany(companyNow);
            List<CustomCustomerEntity> list = customCustomerService.list(new QueryWrapper<CustomCustomerEntity>()
                    .eq("enterprise_name", dto.getEnterpriseName())
                    .eq("customer_company", companyNow));
            if (!list.isEmpty()){
                R.fail("本公司该客户已存在,请检查客户名称");
            }
        }else {
            return R.fail("当前用户查询不到所属公司,请联系管理员处理");
        }
        return customCustomerService.newCustomer(dto) ? R.ok(null, "新建客户成功") : R.fail(null, "新建客户失败");
    }

    /**
     * 客户以及联系人修改
     * @param dto
     * @return R
     */
    @PostMapping("/updateCustomer")
    public R<String> updateCustomer(@RequestBody CustomCustomerDto dto){
        return customCustomerService.updateCustomer(dto) ? R.ok(null, "修改客户信息成功") : R.fail(null, "新建客户信息失败");
    }

    /**
     * 查询是否可删除(是否有关联数据)
     * @param customerId
     * @return R
     */
    @GetMapping("/checkIfRelate")
    public R<String> updateCustomer(Long customerId){
        return customCustomerService.checkIfRelate(customerId) ? R.ok(null, "该客户信息不可删除") : R.ok(null, "可以删除");
    }

    /**
     * 删除客户(支持批量删除)
     * @param customerId
     * @return R
     */
    @GetMapping("/deleteCustomer")
    public R<String> deleteCustomer(Long customerId){
        customCustomerService.deleteCustomer(customerId);
        return R.ok(null, "删除成功");
    }

    /**
     * 查询企业信息和联系方式
     * @param customerId
     * @return R
     */
    @GetMapping("/getCompanyAndContact")
    public R<Object> getCompanyAndContact(Long customerId){
        return R.ok( customCustomerService.getCompanyAndContact(customerId), "查询成功" );
    }

    /**
     * 业务员主动领取
     */
    @PostMapping("/receive")
    @ApiOperation("业务员主动领取")
    public R<String> receive(@RequestBody CustomCustomerDto entity){
        CustomAdvanceTaskEntity one = new CustomAdvanceTaskEntity();
        one.setCustomId(entity.getId());
        one.setAdvanceId(SecurityUtils.getUserId());
        one.setBusinessStatus(1);
        return customAdvanceTaskService.add(one) == 1 ? R.ok("领取成功") : R.fail("领取异常");
    }

    /**
     * 客户公海分页
     */
    @GetMapping("/companyPublicList")
    @ApiOperation("客户公海分页")
    public TableDataInfo companyPublicList(CustomCustomerDto dto) {
        String companyOrder = "";
        List<RoleAndCompanyDto> racs = customCustomerService.getRoleAndCompany(SecurityUtils.getUserId());
        String salesMan = "company_salesman", manager = "company_manager", director = "company_director", admin = "admin", administrators = "administrators";
        List<String> roles = new ArrayList<>();
        for (RoleAndCompanyDto rac : racs) {
            roles.add(rac.getRoleKey());
        }
        if (roles.contains(salesMan) || roles.contains(manager)){
            companyOrder = racs.get(0).getDictLabel();
        }
        if (roles.contains(director) || roles.contains(admin) || roles.contains(administrators)){
            companyOrder = "";
        }
        startPage();
        return getDataTable(customCustomerService.selectPublicCompany(dto, companyOrder));
    }
}
