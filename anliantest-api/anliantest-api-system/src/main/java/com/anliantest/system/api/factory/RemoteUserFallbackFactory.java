package com.anliantest.system.api.factory;

import com.anliantest.common.core.domain.R;
import com.anliantest.system.api.RemoteUserService;
import com.anliantest.system.api.domain.SysDept;
import com.anliantest.system.api.domain.SysRole;
import com.anliantest.system.api.domain.SysUser;
import com.anliantest.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {
            @Override
            public R<LoginUser> getUserInfo(String username, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(SysUser sysUser, String source) {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> usersByRoleIds(String roleIds, String source) {
                return R.fail("查询角色下用户:" + throwable.getMessage());
            }

            @Override
            public List<SysRole> roleList() {
                return null;
            }

            @Override
            public SysDept getInfoByDeptId(Long deptId, String source) {
                return null;
            }

            @Override
            public SysUser getInfoById(Long userId, String source) {
                return null;
            }

            @Override
            public SysUser selectUserByOaUserId(Long oaUserId, String source) {
                return null;
            }

            @Override
            public SysUser selectUserByUserId(Long userId, String source) {
                return null;
            }
        };
    }
}
