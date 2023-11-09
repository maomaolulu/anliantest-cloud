package com.anliantest.system.controller;

import com.anliantest.common.core.constant.CacheConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.core.utils.poi.ExcelUtil;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.domain.AjaxResult;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.common.security.annotation.InnerAuth;
import com.anliantest.common.security.annotation.RequiresPermissions;
import com.anliantest.common.security.utils.DictUtils;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.system.api.domain.SysDept;
import com.anliantest.system.api.domain.SysDictData;
import com.anliantest.system.api.domain.SysRole;
import com.anliantest.system.api.domain.SysUser;
import com.anliantest.system.api.model.LoginUser;
import com.anliantest.system.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 获取用户列表
     */
    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginUser> info(@PathVariable("username") String username) {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }

    /**
     * 注册用户信息
     */
    @InnerAuth
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody SysUser sysUser) {
        String username = sysUser.getUserName();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return R.fail("当前系统没有开启注册功能！");
        }
        if (!userService.checkUserNameUnique(sysUser)) {
            return R.fail("保存用户'" + username + "'失败，注册账号已存在");
        }
        return R.ok(userService.registerUser(sysUser));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = userService.selectUserById(SecurityUtils.getUserId());
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 根据用户编号获取详细信息
     */
    @RequiresPermissions("system:user:query")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 通过用户ID查询用户
     * @param userId
     * @return
     */
    @InnerAuth
    @GetMapping(value = {"/getInfoById/{userId}"})
    public SysUser getInfoById(@PathVariable("userId") Long userId) {

        SysUser sysUser = userService.selectUserById(userId);
        return sysUser;
    }



    /**
     * 新增用户
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("")
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (!userService.checkUserNameUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        } else if (StringUtils.isNotEmpty(user.getJobNum()) && !userService.checkJobNum(user.getJobNum())) {
            return error("新增用户'" + user.getUserName() + "'失败，用户工号已存在");
        }
        if (user.getDeptId() != null) {
            saveCompany(user);

            Long companyId = deptService.getCompanyId(user.getDeptId());
            user.setCompanyId(companyId);

        }else {
            return error("新增用户需要选择部门");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

//    @PostMapping("ces")
//    public void ces(){
//        List<SysUser> list = userService.list();
//        for (SysUser sysUser : list) {
//            try {
//                Long deptId = sysUser.getDeptId();
//                Long companyId = deptService.getCompanyId(deptId);
//                sysUser.setCompanyId(companyId);
//                userService.updateUser(sysUser);
//            }catch (Exception e){
//                System.err.println(sysUser.toString());
//
//                return;
//            }
//
//        }
//
//
//    }
    /**
     * 修改用户
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        if (user.getDeptId() != null) {
            saveCompany(user);
            Long companyId = deptService.getCompanyId(user.getDeptId());
            user.setCompanyId(companyId);
        } else {
            //修改时，若传deptId,则置空
            user.setCompanyName("");
            user.setCompanyKey("");
            return error("用户部门必须选择");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 新增或修改时，指定部门，则对应处理companyKey与companyName
     *
     * @param user
     */
    private void saveCompany(SysUser user) {
        SysDept sysDept = deptService.selectDeptById(user.getDeptId());
        if (sysDept != null) {
            if (StringUtils.isNotBlank(sysDept.getCompanyKey())) {
                user.setCompanyKey(sysDept.getCompanyKey());
                String companyName = "";
                List<SysDictData> sysDictDataList = DictUtils.getDictCache(CacheConstants.SYS_EHS_COMPANY);
                if (sysDictDataList != null && sysDictDataList.size() > 0) {
                    SysDictData sysDictData = sysDictDataList.stream().filter(t -> sysDept.getCompanyKey().equals(t.getDictValue())).collect(Collectors.toList()).get(0);
                    if (sysDictData != null) {
                        companyName = sysDictData.getDictLabel();
                    }
                }
                user.setCompanyName(companyName);
            }
            if (StringUtils.isBlank(user.getRemark())) {
                user.setRemark(sysDept.getDeptName());
            }
        }
    }

    /**
     * 删除用户
     */
    @RequiresPermissions("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, SecurityUtils.getUserId())) {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @RequiresPermissions("system:user:query")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
    @RequiresPermissions("system:user:list")
    @GetMapping("/deptTree")
    public AjaxResult deptTree(SysDept dept) {
        return success(deptService.selectDeptTreeList(dept));
    }

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/get/oaUserId/{oaUserId}")
    public SysUser selectUserByOaUserId(@PathVariable("oaUserId") Long oaUserId) {
        return userService.selectUserByOaUserId(oaUserId);
    }

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/get/userId/{userId}")
    public SysUser selectUserByUserId(@PathVariable("userId") Long userId) {
        return userService.selectUserById(userId);
    }
    /**
     * 用户列表（用于映射下拉）
     */

    @GetMapping("/listUser")
    public R listUser(String nickName) {
        List<SysUser> list = userService.list(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getUserId, SysUser::getUserName, SysUser::getDeptId, SysUser::getNickName)
                .like(StringUtils.isNotBlank(nickName), SysUser::getNickName, nickName));
        Map<Long, List<SysUser>> collect = list.stream().collect(Collectors.groupingBy(SysUser::getDeptId));
        HashMap<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("map",collect);
        return R.ok(map);
    }
}
