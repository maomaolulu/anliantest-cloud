package com.anliantest.data.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.datascope.util.DataScopeUtil;
import com.anliantest.data.domain.dto.ProcessConfigurationDto;
import com.anliantest.system.api.RemoteUserService;
import com.anliantest.system.api.domain.SysRole;
import com.anliantest.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anliantest.data.entity.ProcessConfiguration;
import com.anliantest.data.mapper.ProcessConfigurationMapper;
import com.anliantest.data.service.ProcessConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhanghao
 * @date 2023-07-17
 * @desc : 流程配置表
 */
@Service
public class ProcessConfigurationServiceImpl extends ServiceImpl<ProcessConfigurationMapper, ProcessConfiguration> implements ProcessConfigurationService {

    @Autowired
    private RemoteUserService remoteUserService;

    @Override
    public List<ProcessConfigurationDto> processConfigurationList(ProcessConfigurationDto processConfigurationDto) {
        String d = DataScopeUtil.getScopeSql("d", "");
        List<ProcessConfigurationDto> processConfigurationDtos = baseMapper.processConfigurationDtoListPage(new QueryWrapper<ProcessConfiguration>()
                //项目
                .eq("pc.type", 0)
                // 子类型 （项目：0：项目下发，1：公示审批，2：结束确认）
                .eq("pc.subtype", processConfigurationDto.getSubtype())
                //公司查询
                .eq(processConfigurationDto.getCompanyId() != null, "pt.company_id", processConfigurationDto.getCompanyId())
                //类型编号查询
                .like(StrUtil.isNotBlank(processConfigurationDto.getCode()), "pt.`code`", processConfigurationDto.getCode())
                //类型名称查询
                .like(StrUtil.isNotBlank(processConfigurationDto.getName()), "pt.`name`", processConfigurationDto.getName())
                //数据权限
                .apply(StrUtil.isNotBlank(d), d)
        );


        if (processConfigurationDtos != null && processConfigurationDtos.size() > 0) {
            List<SysRole> sysRoles = remoteUserService.roleList();
            Map<Long, List<SysRole>> collect1 = sysRoles.stream().collect(Collectors.groupingBy(SysRole::getRoleId));
            for (ProcessConfigurationDto processConfigurationDto1 : processConfigurationDtos
            ) {
                String roleIds = processConfigurationDto1.getRoleIds();
                if (StrUtil.isNotBlank(roleIds)) {
                    String[] split = roleIds.split(",");

                    ArrayList<String> strings = new ArrayList<>();
                    for (String s : split
                    ) {
                        Long aLong = Long.valueOf(s);
                        List<SysRole> sysRoles1 = collect1.get(aLong);
                        if (CollUtil.isNotEmpty(sysRoles1)) {
                            strings.add(sysRoles1.get(0).getRoleName());
                        }

                    }
                    if (CollUtil.isNotEmpty(strings)) {
                        String join = String.join(",", strings);
                        processConfigurationDto1.setRoleNames(join);
                    }

                    R<List<SysUser>> listR = remoteUserService.usersByRoleIds(processConfigurationDto1.getRoleIds(), SecurityConstants.INNER);
                    List<SysUser> data = listR.getData();
                    if (data != null && data.size() > 0) {
                        String collect = data.stream().distinct().map(SysUser::getNickName).collect(Collectors.joining(","));
                        processConfigurationDto1.setRoleUserNames(collect);

                    }
                }
            }

        }


        return processConfigurationDtos;
    }
}


