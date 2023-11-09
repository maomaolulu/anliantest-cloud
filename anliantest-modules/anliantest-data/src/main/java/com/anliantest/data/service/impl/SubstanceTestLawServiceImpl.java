package com.anliantest.data.service.impl;

import com.anliantest.common.core.enums.DeleteFlag;
import com.anliantest.common.core.enums.Numbers;
import com.anliantest.common.core.utils.ObjectUtils;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.data.domain.dto.SubstanceTestLawDto;
import com.anliantest.data.entity.SubstanceTestLawEntity;
import com.anliantest.data.entity.SubstanceTestMethodEntity;
import com.anliantest.data.mapper.SubstanceTestLawMapper;
import com.anliantest.data.service.SubstanceTestLawService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author gy
 * @date 2023-06-06
 */
@Service
public class SubstanceTestLawServiceImpl extends ServiceImpl<SubstanceTestLawMapper, SubstanceTestLawEntity> implements SubstanceTestLawService {

//    @Autowired
//    private SubstanceTestMethodService substanceTestMethodService;

    @Override
    public List<SubstanceTestLawEntity> listByCondition(SubstanceTestLawDto substanceTestLawdto){
        return this.list(getSelectWrapper(substanceTestLawdto));
    }

    @Override
    public Boolean saveByCondition(SubstanceTestLawDto substanceTestLawDto){
        SubstanceTestLawEntity entity = ObjectUtils.transformObj(substanceTestLawDto,SubstanceTestLawEntity.class);
//        entity.setCreateBy(ShiroUtils.getUserName());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        setStatusByNowtime(entity);
        return this.save(entity);
    }

    @Override
    public Boolean updateByCondition(SubstanceTestLawDto substanceTestLawDto){
        SubstanceTestLawEntity entity = ObjectUtils.transformObj(substanceTestLawDto,SubstanceTestLawEntity.class);
//        entity.setUpdateBy(ShiroUtils.getUserName());
        entity.setUpdateTime(new Date());
        setStatusByNowtime(entity);
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByCondition(Long id){
        SubstanceTestLawEntity entity = this.getById(id);
        entity.setDeleteFlag(DeleteFlag.YES.ordinal());
        if (ObjectUtil.isNotNull(entity.getTestCategory())){
            // TODO 逻辑删除物质检测方法,若关联资质则不可删除
            SubstanceTestMethodEntity method = new SubstanceTestMethodEntity();
            method.setDeleteFlag(DeleteFlag.YES.ordinal());
//            substanceTestMethodService.update(method,new QueryWrapper<SubstanceTestMethodEntity>().eq("substance_test_law_id",id));
        }
        return updateById(entity);
    }

    private QueryWrapper<SubstanceTestLawEntity> getSelectWrapper(SubstanceTestLawDto substanceTestLawdto){
        return new QueryWrapper<SubstanceTestLawEntity>()
                .eq(ObjectUtil.isNotNull(substanceTestLawdto.getId()),"id",substanceTestLawdto.getId())
                .like(StringUtils.isNotBlank(substanceTestLawdto.getTestStandards()),"test_standards",substanceTestLawdto.getTestStandards())
                .like(StringUtils.isNotBlank(substanceTestLawdto.getTestStandardsName()),"test_standards_name",substanceTestLawdto.getTestStandardsName())
                .eq(StringUtils.isNotBlank(substanceTestLawdto.getLegalEffect()),"legal_effect",substanceTestLawdto.getLegalEffect())
                .eq("delete_flag", Numbers.ZERO.ordinal())
                .eq(ObjectUtil.isNotNull(substanceTestLawdto.getStatus()),"status",substanceTestLawdto.getStatus())
                .isNotNull(substanceTestLawdto.getStandCategory() == Numbers.ZERO.ordinal(),"test_category")
                .isNull(substanceTestLawdto.getStandCategory() == Numbers.FIRST.ordinal(),"test_category")
                .orderByDesc("update_time");
    }

    private void setStatusByNowtime(SubstanceTestLawEntity entity){
        Date now = new Date();
        Date startDate = entity.getImplementationDate();
        Date endDate = entity.getAbolitionDate();
        if (ObjectUtil.isNotNull(startDate)){
            if (now.getTime() < startDate.getTime()){
                entity.setStatus(Numbers.TWO.ordinal());
            }else {
                entity.setStatus(Numbers.FIRST.ordinal());
                if (ObjectUtil.isNotNull(endDate)){
                    if (now.getTime() > endDate.getTime()){
                        entity.setStatus(Numbers.ZERO.ordinal());
                    }
                }
            }
        }

    }

    @Override
    public void refreshStatus(){
        Date now = new Date();
        SubstanceTestLawEntity entity = new SubstanceTestLawEntity();
        // 发布
        entity.setStatus(Numbers.TWO.ordinal());
        this.update(entity,new QueryWrapper<SubstanceTestLawEntity>().lt("implementation_date",now));
        // 现行
        entity.setStatus(Numbers.FIRST.ordinal());
        this.update(entity,new QueryWrapper<SubstanceTestLawEntity>().ge("implementation_date",now).and(warpper ->warpper.lt("abolition_date",now).or().isNull("abolition_date")));
        // 废止
        entity.setStatus(Numbers.ZERO.ordinal());
        this.update(entity,new QueryWrapper<SubstanceTestLawEntity>().ge("abolition_date",now));
    }
}
