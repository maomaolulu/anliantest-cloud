package com.anliantest.file.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.enums.rocketmq.MessageCodeEnum;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.file.api.domain.ProgressManage;
import com.anliantest.file.service.ProgressManageService;
import com.anliantest.rocketmq.api.RemoteRocketMqSendService;
import com.anliantest.rocketmq.api.domain.RocketMqSend;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhanghao
 * @date 2023-08-29
 * @desc : 进度管理
 */
@RestController
@Api(tags = "进度管理")
@RequestMapping("/progress_manage")
public class ProgressManageController extends BaseController {

    private final ProgressManageService progressManageService;
    private final RemoteRocketMqSendService remoteRocketMqSendService;
    @Autowired
    public ProgressManageController(ProgressManageService progressManageService, RemoteRocketMqSendService remoteRocketMqSendService) {
        this.progressManageService = progressManageService;
        this.remoteRocketMqSendService = remoteRocketMqSendService;
    }


    /**
     * 进度管理分页
     * @param progressManage
     * @return
     */
    @GetMapping("listPage")
    public TableDataInfo listPage(ProgressManage progressManage){
        startPage();
        List<ProgressManage> progressManages = progressManageService.listPage(progressManage );
        return getDataTable(progressManages);
    }




    /**
     * 新增进度管理
     * @param progressManage
     * @return
     */
    @PostMapping("save")
    public R save(@RequestBody ProgressManage progressManage ){
        if(StrUtil.isBlank(progressManage.getExportType())){
            return R.fail("导出类型不能为空");
        }
        progressManage.setContent("定时任务");
        progressManage.setCreateBy(SecurityUtils.getUsernameCn());
        progressManage.setCreateById(SecurityUtils.getUserId());

         progressManageService.save(progressManage);

        String s = JSONUtil.toJsonStr(progressManage);
        remoteRocketMqSendService.sendSynchronizeMessage(RocketMqSend.builder().topic(MessageCodeEnum.PROGRESS_MANAGE.getCode()).tag(progressManage.getExportType()).messageText(s).build(),
                SecurityConstants.INNER)  ;
        return R.ok("新增成功");
    }


    /**
     * 修改进度管理
     * @param progressManage
     * @return
     */
    @PostMapping("update")
    public R update(@RequestBody  ProgressManage progressManage){

        progressManageService.updateById(progressManage);
        return R.ok("修改成功");
    }

   /**
     * 删除进度管理
     */
    @DeleteMapping("/del")
    public R remove(@RequestBody List<ProgressManage> progressManages) {
        List<Long> ids = progressManages.stream().map(ProgressManage::getId).collect(Collectors.toList());
        progressManageService.removeByIds(ids);
        return R.ok("删除成功");
    }




}
