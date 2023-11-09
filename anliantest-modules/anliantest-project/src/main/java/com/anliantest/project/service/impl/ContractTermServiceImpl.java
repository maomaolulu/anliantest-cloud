package com.anliantest.project.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.anliantest.common.core.enums.DeleteFlag;
import com.anliantest.common.core.enums.Numbers;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.project.domain.dto.ContractTermDto;
import com.anliantest.project.entity.ContractField;
import com.anliantest.project.entity.ContractSampleTerm;
import com.anliantest.project.entity.ContractTerm;
import com.anliantest.project.entity.ContractTermField;
import com.anliantest.project.mapper.ContractTermMapper;
import com.anliantest.project.service.ContractSampleTermService;
import com.anliantest.project.service.ContractTermFieldService;
import com.anliantest.project.service.ContractTermService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhanghao
 * @date 2023-10-16
 * @desc : 合同条款表
 */
@Service
public class ContractTermServiceImpl extends ServiceImpl<ContractTermMapper, ContractTerm> implements ContractTermService {

    private final ContractTermFieldService contractTermFieldService;
    private final ContractSampleTermService contractSampleTermService;

    @Autowired
    public ContractTermServiceImpl(ContractTermFieldService contractTermFieldService,
    ContractSampleTermService contractSampleTermService){
        this.contractTermFieldService = contractTermFieldService;
        this.contractSampleTermService = contractSampleTermService;
    }

    /**
     * 新增条款
     */
    @Override
    @Transactional
    public Boolean add(ContractTermDto contractTermDto) {

        //处理条款参数
        ContractTerm contractTerm = new ContractTerm();
        contractTerm.setCompanyId(contractTermDto.getCompanyId());
        contractTerm.setTermTypeId(contractTermDto.getTermTypeId());
        contractTerm.setContent(contractTermDto.getContent());
        contractTerm.setContentType(contractTermDto.getContentType());
        contractTerm.setTermStatus(contractTermDto.getTermStatus());
        contractTerm.setCreateBy(SecurityUtils.getUsernameCn());
        contractTerm.setCreateTime(new Date());
        contractTerm.setUpdateBy(SecurityUtils.getUsernameCn());
        contractTerm.setUpdateTime(new Date());
        //拼接 关联字段(中文)拼接字符串
        List<ContractField> fieldList = contractTermDto.getFieldList();
        //含字段，条款关联字段列表不为空，且状态不是待开发状态
        if (CollectionUtil.isNotEmpty(fieldList) && contractTermDto.getTermStatus() != Numbers.TWO.ordinal()) {
            StringBuilder associatedFields = new StringBuilder();
            for (int i = 0; i < fieldList.size(); i++) {
                if (i == fieldList.size() - 1) {
                    associatedFields.append(fieldList.get(i).getName());
                    continue;
                }
                associatedFields.append(fieldList.get(i).getName()).append(",");
            }
            contractTerm.setAssociatedFields(associatedFields.toString());
        }
        this.save(contractTerm);
        //关联表
        List<ContractTermField> termFieldList = new ArrayList<>();
        for (ContractField contractField : fieldList) {
            ContractTermField contractTermField = new ContractTermField();
            contractTermField.setContractTermId(contractTerm.getId());
            contractTermField.setContractFieldId(contractField.getId());
            contractTermField.setFieldDescribe(contractField.getFieldDescribe());
            termFieldList.add(contractTermField);
        }
        contractTermFieldService.saveBatch(termFieldList);
        return true;
    }


    /**
     * 条款列表查询
     */
    @Override
    public List<ContractTerm> getList(ContractTermDto contractTermDto) {

        QueryWrapper<ContractTerm> queryWrapper = new QueryWrapper<ContractTerm>()
                .eq(contractTermDto.getCompanyId() != null, "t.company_id", contractTermDto.getCompanyId())
                .eq(contractTermDto.getTermTypeId() != null, "t.term_type_id", contractTermDto.getTermTypeId())
                .eq(contractTermDto.getContentType() != null, "t.content_type", contractTermDto.getContentType())
                .eq(contractTermDto.getTermStatus() != null, "t.term_status", contractTermDto.getTermStatus())
                .eq("t.del_flag", DeleteFlag.NO.ordinal())
                .like(StringUtils.isNotBlank(contractTermDto.getContent()), "t.content", contractTermDto.getContent())
                .like(StringUtils.isNotBlank(contractTermDto.getAssociatedFields()), "t.associated_fields", contractTermDto.getAssociatedFields())
                .like(StringUtils.isNotBlank(contractTermDto.getUpdateBy()), "t.update_by", contractTermDto.getUpdateBy())
                .orderByDesc("t.term_status")
                .orderByDesc("t.update_time");
        return baseMapper.getList(queryWrapper);
    }


    /**
     * 条款详情查询
     */
    @Override
    public ContractTerm getInfo(Long id) {

        ContractTerm contractTerm = baseMapper.getList(new QueryWrapper<ContractTerm>()
                .eq(id !=null,"t.id",id)).get(0);

        List<ContractField> contractFieldList = new ArrayList<>();
        //内容类型为含字段(contentType = 1) 查所关联字段列表
        if (contractTerm.getContentType() == Numbers.FIRST.ordinal()) {
            QueryWrapper<ContractTerm> queryWrapper = new QueryWrapper<ContractTerm>().eq(id != null, "tf.contract_term_id", id);
            contractFieldList = baseMapper.getInfo(queryWrapper);
        }
        contractTerm.setFieldList(contractFieldList);

        return contractTerm;
    }


    /**
     * 编辑条款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTerm(ContractTermDto contractTermDto) {

        //处理条款参数
        ContractTerm contractTerm = new ContractTerm();
        contractTerm.setId(contractTermDto.getId());
        contractTerm.setCompanyId(contractTermDto.getCompanyId());
        contractTerm.setTermTypeId(contractTermDto.getTermTypeId());
        contractTerm.setContent(contractTermDto.getContent());
        contractTerm.setContentType(contractTermDto.getContentType());
        contractTerm.setTermStatus(contractTermDto.getTermStatus());
        contractTerm.setUpdateBy(SecurityUtils.getUsernameCn());
        contractTerm.setUpdateTime(new Date());
        contractTerm.setAssociatedFields("");
        //拼接 关联字段(中文)拼接字符串
        List<ContractField> fieldList = contractTermDto.getFieldList();
        //含字段，条款关联字段列表不为空，且状态不是待开发状态
        if (CollectionUtil.isNotEmpty(fieldList) && contractTermDto.getTermStatus() != Numbers.TWO.ordinal()) {
            StringBuilder associatedFields = new StringBuilder();
            for (int i = 0; i < fieldList.size(); i++) {
                if (i == fieldList.size() - 1) {
                    associatedFields.append(fieldList.get(i).getName());
                    continue;
                }
                associatedFields.append(fieldList.get(i).getName()).append(",");
            }
            contractTerm.setAssociatedFields(associatedFields.toString());
        }
        this.updateById(contractTerm);
        //删除原来的关联表数据
        contractTermFieldService.remove(new QueryWrapper<ContractTermField>()
                .eq(contractTermDto.getId() != null, "contract_term_id", contractTermDto.getId()));
        //存入新的关联表数据
        List<ContractTermField> termFieldList = new ArrayList<>();
        for (ContractField contractField : fieldList) {
            ContractTermField contractTermField = new ContractTermField();
            contractTermField.setContractTermId(contractTerm.getId());
            contractTermField.setContractFieldId(contractField.getId());
            contractTermField.setFieldDescribe(contractField.getFieldDescribe());
            termFieldList.add(contractTermField);
        }
        contractTermFieldService.saveBatch(termFieldList);

        return true;
    }

    /**
     * 编辑条款状态，停用/启用
     */
    @Override
    public Boolean updateStatus(ContractTermDto contractTermDto) {

        ContractTerm contractTerm = new ContractTerm();
        contractTerm.setId(contractTermDto.getId());
        contractTerm.setTermStatus(contractTermDto.getTermStatus());

        return this.updateById(contractTerm);
    }

    /**
     * 校验是否存在关联合同范本
     */
    @Override
    public Boolean check(Long id) {

        List<ContractSampleTerm> list = contractSampleTermService.list(new QueryWrapper<ContractSampleTerm>()
                .eq(id != null, "contract_term_id", id));
        return CollectionUtil.isEmpty(list);
    }


    /**
     * 删除条款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeTerm(Long id) {

        //逻辑删除条款
        this.update(new UpdateWrapper<ContractTerm>()
                .eq(id != null, "id", id)
                .set("update_time", new Date())
                .set("update_by", SecurityUtils.getUsernameCn())
                .set("del_flag", DeleteFlag.YES.ordinal()));

        //删除关联表
        contractTermFieldService.remove(new QueryWrapper<ContractTermField>().eq(id != null, "contract_term_id", id));

        return true;
    }

    @Override
    public List<ContractTerm> listByTermTypeId(ContractTerm contractTerm) {

        return this.list(new LambdaQueryWrapper<ContractTerm>()
                .eq(ContractTerm::getDelFlag, 0)
                .eq(ContractTerm::getTermStatus, 1)
                .eq(ContractTerm::getTermTypeId, contractTerm.getTermTypeId())
                .like(StrUtil.isNotBlank(contractTerm.getContent()), ContractTerm::getContent, contractTerm.getContent())
        );
    }
}
