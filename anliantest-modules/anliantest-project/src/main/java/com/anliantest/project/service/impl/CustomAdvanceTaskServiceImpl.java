package com.anliantest.project.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.StatefulException;
import com.anliantest.common.redis.service.RedisService;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.project.domain.dto.CustomAdvanceDto;
import com.anliantest.project.domain.dto.RoleAndCompanyDto;
import com.anliantest.project.domain.vo.CustomAdvanceVo;
import com.anliantest.project.entity.CustomAdvanceTaskEntity;
import com.anliantest.project.entity.CustomCustomerEntity;
import com.anliantest.project.mapper.CustomAdvanceTaskMapper;
import com.anliantest.project.mapper.CustomCustomerMapper;
import com.anliantest.project.service.CustomAdvanceTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/21 11:46
 * @Version 1.0
 * @Description 跟进任务
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CustomAdvanceTaskServiceImpl extends ServiceImpl<CustomAdvanceTaskMapper, CustomAdvanceTaskEntity> implements CustomAdvanceTaskService {
    private final CustomAdvanceTaskMapper customAdvanceTaskMapper;
    private final RedisService redisService;
    private final CustomCustomerMapper customCustomerMapper;

    public CustomAdvanceTaskServiceImpl(CustomAdvanceTaskMapper customAdvanceTaskMapper,
                                        RedisService redisService, CustomCustomerMapper customCustomerMapper) {
        this.customAdvanceTaskMapper = customAdvanceTaskMapper;
        this.redisService = redisService;
        this.customCustomerMapper = customCustomerMapper;
    }

    /**
     * 更新任务信息
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @Override
    public int modify(CustomAdvanceTaskEntity customAdvanceTask) {
        if (customAdvanceTask.getAdvanceResult() != null) {
            // 跟进结束
            customAdvanceTask.setBusinessStatus(5);
            // 修改客户隶属
            CustomAdvanceTaskEntity task = customAdvanceTaskMapper.selectById(customAdvanceTask.getId());
            CustomCustomerEntity customer = new CustomCustomerEntity();
            customer.setId(task.getCustomId());
            // 1 老客户
            customer.setIfNewCompany(1);
            customer.setCustomerOrder(null);
            customCustomerMapper.updateById(customer);
        }
        customAdvanceTask.setUpdateBy(SecurityUtils.getUsernameCn());
        customAdvanceTask.setUpdateTime(new Date());
        return customAdvanceTaskMapper.updateById(customAdvanceTask);
    }

    /**
     * 人员替换
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @Override
    public int replaceUserBatch(CustomAdvanceTaskEntity customAdvanceTask) {
        Long userId = customAdvanceTask.getUserId();
        List<CustomAdvanceTaskEntity> tasksList = customAdvanceTask.getTasksList();
        for (CustomAdvanceTaskEntity task : tasksList) {
            CustomAdvanceTaskEntity advanceTask = new CustomAdvanceTaskEntity();
            advanceTask.setId(task.getTaskId());
            advanceTask.setAdvanceId(userId);
            advanceTask.setUpdateTime(new Date());
            advanceTask.setUpdateBy(SecurityUtils.getUsernameCn());
            if (customAdvanceTaskMapper.updateById(advanceTask) == 0) {
                throw new StatefulException("替换人员发生异常");
            }
        }
        return 1;
    }

    /**
     * 新建任务
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @Override
    public int add(CustomAdvanceTaskEntity customAdvanceTask) {
        customAdvanceTask.setCreateTime(new Date());
        customAdvanceTask.setCreateBy(SecurityUtils.getUsernameCn());
        customAdvanceTask.setTaskCode(getCode());
        return customAdvanceTaskMapper.insert(customAdvanceTask);
    }

    /**
     * 任务分配
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    @Override
    public int allocationBatch(CustomAdvanceTaskEntity customAdvanceTask) {
        Long userId = customAdvanceTask.getUserId();
        List<CustomAdvanceTaskEntity> tasksList = customAdvanceTask.getTasksList();
        for (CustomAdvanceTaskEntity cus : tasksList) {
            cus.setAdvanceId(userId);
            cus.setCustomId(cus.getCustomerId());
            cus.setBusinessStatus(1);
            cus.setCreateTime(new Date());
            cus.setCreateBy(SecurityUtils.getUsernameCn());
            cus.setTaskCode(getCode());
            if (customAdvanceTaskMapper.insert(cus) == 0) {
                throw new StatefulException("创建新任务失败");
            }
        }
        return 1;

//        List<CustomAdvanceTaskEntity> tasksList = customAdvanceTask.getTasksList();
//        for (CustomAdvanceTaskEntity cus : tasksList) {
//            Integer businessStatus = cus.getBusinessStatus();
//            if (businessStatus == 0) {
//                // 待分配->分配用户
//                cus.setAdvanceId(customAdvanceTask.getUserId());
//                cus.setBusinessStatus(1);
//                if (customAdvanceTaskMapper.updateById(cus) == 0) {
//                    throw new StatefulException("分配任务失败");
//                }
//            } else {
//                // 跟进结束->创建任务
//                cus.setAdvanceId(customAdvanceTask.getUserId());
//                cus.setBusinessStatus(1);
//                if (customAdvanceTaskMapper.insert(cus) == 0) {
//                    throw new StatefulException("分配任务时创建新任务失败");
//                }
//            }
//        }
//        return 1;
    }

    private synchronized String getCode() {
        String key = "cus_adv_code";
        Object object = redisService.getCacheObject(key);
        if (object == null) {
            redisService.setCacheObject(key, 2);
            object = 1;
        } else {
            int i = Integer.parseInt(object.toString());
            redisService.setCacheObject(key, ++i);
        }
        return (DateUtil.format(new Date(), "yyyyMMdd") + object).substring(2);
    }

    @Override
    public List<CustomAdvanceVo> listTasks(CustomAdvanceDto dto) {
        QueryWrapper<Object> queryWrapper = getWrapper(dto);
        checkRoles(queryWrapper);
        return baseMapper.listTasks(queryWrapper);
    }

    @Override
    public boolean releaseCompany(CustomAdvanceTaskEntity dto) {
        CustomAdvanceTaskEntity taskUpdate = new CustomAdvanceTaskEntity();
        taskUpdate.setId(dto.getId());
        taskUpdate.setBusinessStatus(5);
        CustomCustomerEntity companyUpdate = new CustomCustomerEntity();
        companyUpdate.setId(dto.getCustomId());
        companyUpdate.setIfNewCompany(1);
        return customCustomerMapper.updateById(companyUpdate) > 0 && this.updateById(taskUpdate);
    }

    @Override
    public void releaseCompanyJob() {
        Date twoYearBefore = DateUtil.offset(new Date(), DateField.YEAR, -2);
        Date threeMonthBefore = DateUtil.offsetMonth(new Date(), -3);
        QueryWrapper<Object> wrapper1 = new QueryWrapper<>();
        QueryWrapper<Object> wrapper2 = new QueryWrapper<>();
        wrapper1.isNotNull("cat1.advance_first_time");
        wrapper1.eq("tc.if_new_company", 0);
        wrapper1.le("cat1.advance_first_time", twoYearBefore);
        wrapper2.isNotNull("cat2.advance_last_time");
        wrapper2.eq("tc.if_new_company", 1);
        wrapper2.le("cat2.advance_last_time", threeMonthBefore);
        List<Long> list1 = baseMapper.selectToBeOpenData(wrapper1);
        List<Long> list2 = baseMapper.selectToBeOpenData(wrapper2);
        for1 : for (Long id : list1){
            QueryWrapper<CustomAdvanceTaskEntity> query = new QueryWrapper<>();
            query.eq("custom_id", id);
            query.orderByDesc("id");
            List<CustomAdvanceTaskEntity> tasks = customAdvanceTaskMapper.selectList(query);
            if (!tasks.isEmpty()) {
                //所有跟进任务都处于结束状态
                for (CustomAdvanceTaskEntity e : tasks) {
                    if (e.getBusinessStatus() != 5) {
                        continue for1;
                    }
                }
            }
            setPublicCompany(id);
        }

        for1 : for (Long id : list2){
            QueryWrapper<CustomAdvanceTaskEntity> query = new QueryWrapper<>();
            query.eq("custom_id", id);
            query.orderByDesc("id");
            List<CustomAdvanceTaskEntity> tasks = customAdvanceTaskMapper.selectList(query);
            for (CustomAdvanceTaskEntity task :tasks){
                if (task.getAdvanceLastTime() != null){
                    if (task.getAdvanceLastTime().getTime() < threeMonthBefore.getTime()) {
                        // 公海来的客户 任务上次跟进跟当前时间差3个月以上则结束任务
                        task.setBusinessStatus(5);
                        customAdvanceTaskMapper.updateById(task);
                    }
                }
            }
        }
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<Object> getWrapper(CustomAdvanceDto dto) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        wrapper.eq(dto.getUserId() != null, "cat.advance_id", dto.getUserId());
        wrapper.eq(dto.getTaskCode() != null, "cat.task_code", dto.getTaskCode());
        wrapper.eq(dto.getCustomerOrder() != null, "IF(cc.if_new_company = 0 && cc.customer_order != '',cc.customer_order,cc.customer_company)", dto.getCustomerOrder());
        wrapper.eq(dto.getEnterpriseName() != null, "cc.enterprise_name", dto.getEnterpriseName());
        wrapper.eq(dto.getContacterName() != null, "ccs.contacter_name", dto.getContacterName());
        wrapper.eq(dto.getMobilePhone() != null, "ccs.mobile_phone", dto.getMobilePhone());
        wrapper.eq(dto.getBusinessStatus() != null, "cat.business_status", dto.getBusinessStatus());
        wrapper.eq(dto.getAdvanceResult() != null, "cat.advance_result", dto.getAdvanceResult());
        wrapper.eq(dto.getAdvanceName() != null, "u.nick_name", dto.getAdvanceName());
        // 查询时间段
        wrapper.ge(dto.getAdvanceFirstTimeStart() != null, "cat.advance_first_time", dto.getAdvanceFirstTimeStart());
        wrapper.le(dto.getAdvanceFirstTimeEnd() != null, "cat.advance_first_time", dto.getAdvanceFirstTimeEnd());
        wrapper.ge(dto.getAdvanceLastTimeStart() != null, "cat.advance_last_time", dto.getAdvanceLastTimeStart());
        wrapper.le(dto.getAdvanceLastTimeEnd() != null, "cat.advance_last_time", dto.getAdvanceLastTimeEnd());
        return wrapper;
    }

    private void checkRoles(QueryWrapper<Object> queryWrapper) {
        List<RoleAndCompanyDto> racs = customCustomerMapper.getRoleAndCompany(SecurityUtils.getUserId());
        String company = "";
        List<String> roles = new ArrayList<>();
        if (racs.size() > 0) {
            company = racs.get(0).getDictLabel();
        }
        for (RoleAndCompanyDto rac : racs) {
            roles.add(rac.getRoleKey());
        }
        if (roles.contains("company_manager")) {
            queryWrapper.eq("cc.customer_company", company);
        }
    }

    /**
     * 客户隶属设置为公司
     */
    private void setPublicCompany(Long companyId){
        CustomCustomerEntity update = new CustomCustomerEntity();
        update.setId(companyId);
        update.setIfNewCompany(1);
        customCustomerMapper.updateById(update);
    }
}
