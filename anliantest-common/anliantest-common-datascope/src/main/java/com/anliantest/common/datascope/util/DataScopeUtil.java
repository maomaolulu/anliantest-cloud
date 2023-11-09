package com.anliantest.common.datascope.util;

import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.system.api.domain.SysRole;
import com.anliantest.system.api.domain.SysUser;
import com.anliantest.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author zx
 * @date 2021/12/9 15:06
 */
@Slf4j
@Component
public class DataScopeUtil {

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";
    /**
     * 公司数据权限
     */
    public static final String DATA_COMPANY_DEPT = "6";

    /**
     * 获取权限sql
     *
     * @param deptAlias 部门表别名
     * @param userAlias 用户表别名
     * @return
     */
    // TODO: 2023-10-16 需要增加一个本公司数据权限
    // TODO: 2023-10-16 数据权限优化： 不同的接口需要判断的权限角色不同  方案：添加角色参数  只获取某些角色的数据权限
    // TODO: 2023-10-16 获取的某些角色参数是否能动态配置  例如 合同范本的数据权限判断角色，建立模块数据权限（唯一标识）对应在系统管理展示配置 ，参数就变为模块数据权限
    // TODO: 2023-10-16 数据权限sql做在mabatispluys的sql中 不再做在mapper中 动态连表生成sql 应该需要参数（数据表中需要做权限的部门、用户字段）


    // TODO: 2023-10-16 所有角色不做公司数据权限 在一开始人员勾选好的公司数据权限，所有功能模块只设立部门数据权限  （功能模块数据一公司为准是一开始勾选好的   以部门为准）
    // TODO: 2023-10-17 获取本人员本模块的角色拥有的deptId  角色数据权限只勾选公司 （  以项目类型的权限已项目类型判断  ） 新增角色的时候需要选择模块
    public static String getScopeSql( String deptAlias, String userAlias) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getSysUser();
        if (user != null) {
            // 如果是超级管理员，则不过滤数据
            if (user.isAdmin()) {
                return "";
            }
        } else {
            log.warn("数据权限拦截失败,执行对象 user is null");
        }
        StringBuilder sqlString = new StringBuilder();
        for (SysRole role : user.getRoles()) {
            String dataScope = role.getDataScope();
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                break;
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope) && StringUtils.isNotBlank(deptAlias)) {
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM `anliantest-system`.sys_role_dept WHERE role_id = {} ) ", deptAlias,
                        role.getRoleId()));
            } else if (DATA_SCOPE_DEPT.equals(dataScope) && StringUtils.isNotBlank(deptAlias)) {
                sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                if ("0".equals(user.getDeptId())) {
                    sqlString = new StringBuilder();
                    break;
                }
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM `anliantest-system`.sys_dept WHERE dept_id = {} or ancestors LIKE '%,{},%' )",
                        deptAlias, user.getDeptId(), user.getDeptId()));
            }

//            else if (DATA_COMPANY_DEPT.equals(dataScope)) {
//                if ("0".equals(user.getDeptId())) {
//                    sqlString = new StringBuilder();
//                    break;
//                }
//                Long companyId = SystemUtil.getCompanyId();
//                sqlString.append(StringUtils.format(
//                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or ancestors LIKE '%,{},%' )",
//                        deptAlias, companyId, companyId));
//            }

            else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                } else {
                    sqlString.append(StringUtils.format(" OR {}.dept_id IS NULL ", deptAlias));
                }
            }
        }
//        String format = StringUtils.isBlank(deptAlias) ? "" : StringUtils.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_user_dept WHERE user_id = {} ) ", deptAlias, user.getUserId());
//        if (StringUtils.isNotBlank(sqlString.toString())) {
//            return "(" + sqlString.substring(4) + format + ")";
//        }
        if(StringUtils.isBlank(sqlString.toString())){
            return "";
        }
        return "(" + sqlString.substring(4) +  ")";
    }
}
