package com.anliantest.system.api;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.system.api.domain.SysDept;
import com.anliantest.system.api.domain.SysRole;
import com.anliantest.system.api.domain.SysUser;
import com.anliantest.system.api.factory.RemoteUserFallbackFactory;
import com.anliantest.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source   请求来源
     * @return 结果
     */
    @GetMapping("/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source  请求来源
     * @return 结果
     */
    @PostMapping("/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 批量查询角色下用户
     *
     * @param roleIds 角色IDs
     * @return 结果
     */
    @GetMapping("/role/usersByRoleIds")
    public R<List<SysUser>> usersByRoleIds(@RequestParam("roleIds") String roleIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 所有角色
     *
     * @return 结果
     */
    @GetMapping("/role/roleList")
    public List<SysRole> roleList();

    /**
     * 部门详情
     *
     * @return 结果
     */
    @GetMapping("dept/info/{deptId}")
    public SysDept getInfoByDeptId(@PathVariable("deptId") Long deptId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过用户ID查询用户
     *
     * @param userId
     * @return
     */
    @GetMapping(value = {"/user/getInfoById/{userId}"})
    SysUser getInfoById(@PathVariable("userId") Long userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/user/get/oaUserId/{oaUserId}")
    SysUser selectUserByOaUserId(@PathVariable("oaUserId") Long oaUserId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/user/get/userId/{userId}")
    SysUser selectUserByUserId(@PathVariable("userId") Long userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
